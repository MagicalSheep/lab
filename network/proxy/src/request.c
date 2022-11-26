#include <arpa/inet.h>
#include <netdb.h>
#include <request.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void req_to_str(struct request *req, char *str)
{
    sprintf(str, "%s %s HTTP/%.1f\r\n", (req->method == GET_METHOD) ? "GET" : "POST", req->path, req->http_ver);
    int len = strlen(str);
    for (int i = 0; i < req->headers_len; i++)
    {
        int delta = strlen(req->headers[i].name) + strlen(req->headers[i].value) + 4;
        sprintf(str + len, "%s: %s\r\n", req->headers[i].name, req->headers[i].value);
        len += delta;
    }
    if (req->body != NULL)
    {
        sprintf(str + len, "\r\n%s\r\n", req->body);
        len += (strlen(req->body) + 4);
    }
    sprintf(str + len, "\r\n");
}

int resolve_dns(struct request *req)
{
    req->dest.sin_family = AF_INET;
    int host_len = strlen(req->host);
    char tmp[host_len];
    strcpy(tmp, req->host);
    char *s = strtok(tmp, ":");
    int result = inet_pton(AF_INET, s, (void *)&req->dest.sin_addr);
    if (result == 0)
    {
        struct addrinfo hints, *res;
        memset(&hints, 0, sizeof(hints));
        hints.ai_family = AF_INET;
        hints.ai_socktype = SOCK_STREAM;
        if (getaddrinfo(s, "http", &hints, &res) != 0)
            return -1;
        struct sockaddr_in *ip = (struct sockaddr_in *)res->ai_addr;
        req->dest.sin_addr = ip->sin_addr;
        freeaddrinfo(res);
    }
    int len = strlen(s);
    if (len == host_len)
        req->dest.sin_port = htons(80);
    else
        req->dest.sin_port = htons(atoi(s + len + 1));
    return 0;
}

int resolve_path(struct request *req, char *raw_path)
{
    char *p = strstr(raw_path, "://");
    if (p != NULL)
        p += 3, p = strtok(p, "/");
    else
        p = strtok(raw_path, "/");
    if (p == NULL)
        return -1;
    int host_len = strlen(p);
    req->host = (char *)malloc(host_len + 1);
    strcpy(req->host, p);
    if (resolve_dns(req) == -1)
        return -3;
    p += host_len;
    p[0] = '/';
    req->path = (char *)malloc(strlen(p) + 1);
    strcpy(req->path, p);
    return 0;
}