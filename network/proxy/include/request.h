#ifndef _REQUEST_H_
#define _REQUEST_H_
#include "picohttpparser.h"
#include <netinet/in.h>
#include <netinet/ip.h>
#define GET_METHOD 1
#define POST_METHOD 2
#define CONNECT_METHOD 3

struct header
{
    char *name, *value;
};

struct request
{
    struct sockaddr_in dest;
    struct header headers[30];
    int method, headers_len;
    double http_ver; // only support 1.0 and 1.1
    char *path, *body, *host;
};

void req_to_str(struct request *req, char *str);

int resolve_dns(struct request *dest);

int resolve_path(struct request *req, char *raw_path);

#endif