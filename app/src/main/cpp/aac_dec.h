#ifndef AP2DEMO_AAC_DEC_H
#define AP2DEMO_AAC_DEC_H

#include "aacdecoder_lib.h"
#include "log.h"

#define LOG_TAG "aac_dec"
#define PCM_BUFF_SIZE 1024 * 1024

typedef struct {
    HANDLE_AACDECODER handle;
    INT_PCM *pcm_buff;
} aac_dec_ctx;

aac_dec_ctx* aac_dec_open();
void aac_dec_close(aac_dec_ctx* ctx);
void aac_dec_decode(aac_dec_ctx* ctx);

#endif //AP2DEMO_AAC_DEC_H
