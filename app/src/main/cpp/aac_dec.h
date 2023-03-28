#ifndef AP2DEMO_AAC_DEC_H
#define AP2DEMO_AAC_DEC_H

#include "stdlib.h"
#include "string.h"
#include "aacdecoder_lib.h"
#include "log.h"

#define PCM_SIZE 480 * 2

typedef struct {
    HANDLE_AACDECODER handle;
    INT_PCM *pcm_buf;
} aac_dec_ctx;

aac_dec_ctx* aac_dec_open();
void aac_dec_close(aac_dec_ctx* ctx);
void aac_dec_decode(aac_dec_ctx* ctx, uint8_t* buf, uint buf_size);

#endif //AP2DEMO_AAC_DEC_H
