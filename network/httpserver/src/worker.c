#include <errno.h>
#include <fcntl.h>
#include <picohttpparser.h>
#include <pthread.h>
#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <unistd.h>
#include <worker.h>

static const char *SUCCESS_HEADER = "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Type: ";
static const char *NOT_FOUND_HEADER = "HTTP/1.1 404 Not Found\r\nConnection: close\r\n\r\n";
static const char *FORBIDDEN_HEADER = "HTTP/1.1 403 Forbidden\r\nConnection: close\r\n\r\n";
static const char *BAD_GATEWAY_HEADER = "HTTP/1.1 502 Bad Gateway\r\nConnection: close\r\n\r\n";
static const char *root = "../public";
static const char *default_page = "index.html";

int brower(char *path, char *buf, size_t *rread, size_t *fsize)
{
    FILE *fp = fopen(path, "r");
    if (fp == NULL)
        return -1;
    if (fseek(fp, 0L, SEEK_END) < 0)
        return -1;
    *fsize = ftell(fp);
    if (fseek(fp, 0L, SEEK_SET) < 0)
        return -1;
    *rread = fread(buf, sizeof(*buf), *fsize, fp);
    if (fclose(fp) < 0)
        return -1;
    return 0;
}

char *get_type(char *path)
{
    int len = strlen(path), st = 0;
    for (int i = len; i >= 0 && !st; i--)
        if (path[i] == '.')
            st = i;
    ++st;
    char *s = path + st;
    if (strcmp("html", s) == 0 || strcmp("htm", s) == 0)
        return "text/html";
    else if (strcmp("css", s) == 0)
        return "text/css";
    else if (strcmp("png", s) == 0)
        return "image/png";
    else if (strcmp("jpeg", s) == 0 || strcmp("jpg", s) == 0)
        return "image/jpeg";
    else if (strcmp("js", s) == 0)
        return "application/x-javascript";
    else if (strcmp("ico", s) == 0)
        return "image/x-icon";
    else if (strcmp("woff2", s) == 0)
        return "font/woff2";
    else
        return "text/plain";
}

void *work(void *args)
{
    int fd = *((int *)args), pret, minor_version;
    char buf[MAX_BUF];
    const char *method, *path;
    struct phr_header headers[30];
    size_t buflen = 0, prevbuflen = 0, method_len, path_len, header_cnt;
    ssize_t rret;

    while (1)
    {
        while ((rret = read(fd, buf + buflen, sizeof(buf) - buflen)) == -1 && errno == EINTR)
            ;
        if (rret <= 0)
        {
            close(fd);
            pthread_exit("Read error");
        }
        prevbuflen = buflen;
        buflen += rret;
        header_cnt = sizeof(headers) / sizeof(headers[0]);
        pret = phr_parse_request(buf, buflen, &method, &method_len, &path, &path_len,
                                 &minor_version, headers, &header_cnt, prevbuflen);
        if (pret > 0)
            break;
        if (pret == -1)
        {
            close(fd);
            pthread_exit("Parse error");
        }
        if (buflen == sizeof(buf))
        {
            close(fd);
            pthread_exit("Request is too long");
        }
    }

    printf("[%ld] %.*s %.*s HTTP/1.%d\n", pthread_self(), (int)method_len, method, (int)path_len, path, minor_version);

    // build real path
    char real_path[100], rbuf[MAX_BUF], res[MAX_BUF << 1];
    if (strncmp("/", path + path_len - 1, 1) == 0)
        sprintf(real_path, "%s%.*s%s", root, (int)path_len, path, default_page);
    else
        sprintf(real_path, "%s%.*s", root, (int)path_len, path);

    // check file exist
    if (access(real_path, F_OK) == -1)
    {
        send(fd, NOT_FOUND_HEADER, strlen(NOT_FOUND_HEADER), 0);
        close(fd);
        printf("[%ld] Invalid resouces %s\n", pthread_self(), real_path);
        pthread_exit("File not found");
    }
    // check file permission
    if (access(real_path, R_OK) == -1)
    {
        send(fd, FORBIDDEN_HEADER, strlen(FORBIDDEN_HEADER), 0);
        close(fd);
        printf("[%ld] Permission denied for resources %s\n", pthread_self(), real_path);
        pthread_exit("Permission denied");
    }

    size_t rread, fsize;
    // try to read
    if (brower(real_path, rbuf, &rread, &fsize) == -1)
    {
        send(fd, BAD_GATEWAY_HEADER, strlen(BAD_GATEWAY_HEADER), 0);
        close(fd);
        printf("[%ld] Load resouces %s failed\n", pthread_self(), real_path);
        pthread_exit("Read file error");
    }

    // build response
    rbuf[fsize++] = '\r';
    rbuf[fsize++] = '\n';
    rbuf[fsize++] = '\r';
    rbuf[fsize++] = '\n';
    sprintf(res, "%s%s\r\nContent-Length: %ld\r\n\r\n", SUCCESS_HEADER, get_type(real_path), fsize);
    int st = strlen(res);
    memcpy(res + st, rbuf, fsize);
    printf("[%ld] Load resouces %s successfully\n", pthread_self(), real_path);

    send(fd, res, st + fsize, 0);
}