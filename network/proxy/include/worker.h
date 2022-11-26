#ifndef _WORKER_H_
#define _WORKER_H_
#include <request.h>
#include <stdlib.h>
#include <sys/types.h>
#define CLEAN(req)                                                             \
    do                                                                         \
    {                                                                          \
        if (req.body != NULL)                                                  \
            free(req.body);                                                    \
        if (req.host != NULL)                                                  \
            free(req.host);                                                    \
        if (req.path != NULL)                                                  \
            free(req.path);                                                    \
        if (req.headers_len > 0 && strcmp(req.headers[0].name, "Cookie") == 0) \
            free(req.headers[0].value);                                        \
    } while (0)
#define BUF_SIZE 65535
#define HEAP_BUF_SIZE 9000000

void *
work(void *args);

ssize_t visit(struct request *req, char *res, int res_len);

#endif