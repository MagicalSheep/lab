#include <arpa/inet.h>
#include <errno.h>
#include <fcntl.h>
#include <picohttpparser.h>
#include <pthread.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <worker.h>

void fail_back(int fd, char *msg, struct request *req)
{
    printf("Error: %s\n", msg);
    if (req != NULL)
        CLEAN((*req));
    char buf[BUF_SIZE];
    sprintf(buf, "HTTP/1.1 502 Bad Gateway\r\nConnection: close\r\n\r\n<html><head><title>502 Bad Gateway</title></head><body><h1>Bad Gateway</h1><p>%s</p></body></html>\r\n\r\n", msg);
    send(fd, buf, strlen(buf), 0);
    close(fd);
    pthread_exit(msg);
}

ssize_t visit(struct request *req, char *res, int res_len)
{
    // rebuild request headers
    // set 'Connection' key to 'close' value
    req->headers[req->headers_len].name = "Connection";
    req->headers[req->headers_len++].value = "close";

    req->headers[req->headers_len].name = "User-Agent";
    req->headers[req->headers_len++].value = "MagicalSheep proxy/1.0";

    req->headers[req->headers_len].name = "Accept";
    req->headers[req->headers_len++].value = "*/*";

    req->headers[req->headers_len].name = "Host";
    req->headers[req->headers_len++].value = req->host;

    req->headers[req->headers_len].name = "Accept-Encoding";
    req->headers[req->headers_len++].value = "gzip, deflate, br";

    char buf[BUF_SIZE];
    memset(buf, 0, sizeof(buf));
    req_to_str(req, buf);

    int fd = socket(AF_INET, SOCK_STREAM, 0);
    if (connect(fd, (struct sockaddr *)&req->dest, sizeof(req->dest)) == -1)
        return -1;
    if (send(fd, buf, strlen(buf), 0) == -1)
        return -1;
    ssize_t rret = recv(fd, res, res_len, 0);
    if (rret != -1)
        res[rret] = 0;
    return rret;
}

void tunnel(int fd, struct request *req)
{
    int tun = socket(AF_INET, SOCK_STREAM, 0);
    if (tun < 0)
        fail_back(tun, "Cannot initialize socket", req);
    if (connect(tun, (struct sockaddr *)&req->dest, sizeof(req->dest)) == -1)
        fail_back(fd, "Cannot open a tunnel for proxy", req);
    printf("[Tunnel][client <--> %s] Established\n", req->host);

    char *connected = "HTTP/1.1 200 Connection Established\r\n\r\n";
    if (send(fd, connected, strlen(connected), 0) == -1)
        goto tun_ed;
    char *buf = (char *)malloc(HEAP_BUF_SIZE);
    ssize_t rret;

    while (1)
    {
        if ((rret = recv(fd, buf, HEAP_BUF_SIZE, 0)) <= 0)
            break;
        printf("[Tunnel][client -> proxy] Receive data from client\n");
        if (send(tun, buf, rret, 0) <= 0)
            break;
        printf("[Tunnel][proxy -> %s] Send data to server\n", req->host);
        if ((rret = recv(tun, buf, HEAP_BUF_SIZE, 0)) <= 0)
            break;
        printf("[Tunnel][%s -> proxy] Receive data from server\n", req->host);
        if (send(fd, buf, rret, 0) <= 0)
            break;
        printf("[Tunnel][proxy -> client] Send data to client\n");
    }
    free(buf);
tun_ed:
    printf("[Tunnel][client <--> %s] Closed\n", req->host);
    close(tun);
}

void *work(void *args)
{
    int fd = *((int *)args), pret, minor_version;
    char buf[BUF_SIZE];
    const char *method, *path;
    struct phr_header headers[30];
    size_t buflen = 0, prevbuflen = 0, method_len, path_len, header_cnt;
    ssize_t rret;
    struct request req;

    while (1)
    {
        while ((rret = read(fd, buf + buflen, sizeof(buf) - buflen)) == -1 && errno == EINTR)
            ;
        if (rret <= 0)
            fail_back(fd, "Read error", &req);
        prevbuflen = buflen;
        buflen += rret;
        header_cnt = sizeof(headers) / sizeof(headers[0]);
        pret = phr_parse_request(buf, buflen, &method, &method_len, &path, &path_len,
                                 &minor_version, headers, &header_cnt, prevbuflen);
        if (pret > 0)
            break;
        else if (pret == -1)
            fail_back(fd, "Parse error", &req);
        if (buflen == sizeof(buf))
            fail_back(fd, "Request is too long", &req);
    }

    char raw_path[200];
    memcpy(raw_path, path, path_len);
    raw_path[path_len] = 0;
    int ret = resolve_path(&req, raw_path);
    if (ret != 0)
    {
        if (ret == -3)
            fail_back(fd, "Unknown hostname", &req);
        fail_back(fd, (ret == -1) ? "Invalild format" : "Not support https", &req);
    }

    if (method_len == 3 && strncmp(method, "GET", 3) == 0)
        req.method = GET_METHOD;
    else if (method_len == 4 && strncmp(method, "POST", 4) == 0)
        req.method = POST_METHOD;
    else if (method_len == 7 && strncmp(method, "CONNECT", 7) == 0)
        req.method = CONNECT_METHOD;
    else
        fail_back(fd, "Invalid or unsupport method", &req);
    if (minor_version == 0)
        req.http_ver = 1.0;
    else
        req.http_ver = 1.1;
    for (int i = 0; i < header_cnt; i++)
    {
        if (strncmp("Content-Length", headers[i].name, 14) == 0)
        {
            int content_len = atoi(headers[i].value);
            req.body = (char *)malloc(content_len + 1);
            memcpy(req.body, buf + buflen - content_len, content_len);
        }
        else if (strncmp("Cookie", headers[i].name, 6) == 0)
        {
            req.headers[req.headers_len].name = "Cookie";
            req.headers[req.headers_len].value = (char *)malloc(headers[i].value_len + 1);
            memcpy(req.headers[req.headers_len].value, headers[i].value, headers[i].value_len);
            ++req.headers_len;
        }
    }

    // when proxy, path is the full url
    // except CONNECT method
    if (req.method == GET_METHOD)
        printf("[client -> %s] %s ", req.host, "GET");
    else if (req.method == POST_METHOD)
        printf("[client -> %s] %s ", req.host, "POST");
    else
        printf("[client -> %s] %s ", req.host, "CONNECT");
    printf("%.*s HTTP/%.1f\n", (int)path_len, path, req.http_ver);

    if (req.method == CONNECT_METHOD)
    {
        tunnel(fd, &req);
        goto end;
    }

    if (visit(&req, buf, sizeof(buf)) == -1)
        fail_back(fd, "Visit error", &req);

    rret = send(fd, buf, strlen(buf), 0);
    if (rret == -1)
        fail_back(fd, "Write error", &req);

    char *s = strchr(buf, '\r');
    if (s != NULL)
    {
        char res[30];
        memcpy(res, buf, s - buf);
        res[s - buf] = 0;
        printf("[%s -> client] %s\n", req.host, res);
    }
    else
    {
        printf("[%s -> client] Empty response\n", req.host);
    }
end:
    CLEAN(req);
    close(fd);
}