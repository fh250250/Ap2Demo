#include "aac_dec.h"

aac_dec_ctx* aac_dec_open() {
    aac_dec_ctx* ctx = malloc(sizeof(aac_dec_ctx));
    if (!ctx) {
        LOGE("malloc aac_dec_ctx error");
        return NULL;
    }

    ctx->pcm_buf = malloc(PCM_BUF_SIZE);
    if (!ctx->pcm_buf) {
        LOGE("malloc pcm_buf error");
        free(ctx);
        return NULL;
    }

    ctx->handle = aacDecoder_Open(TT_MP4_RAW, 1);
    if (!ctx->handle) {
        LOGE("aacDecoder_Open error");
        free(ctx->pcm_buf);
        free(ctx);
        return NULL;
    }

    UCHAR eld_conf[] = { 0xF8, 0xE8, 0x50, 0x00 };
    UCHAR *conf[] = { eld_conf };
    UINT conf_len = sizeof(eld_conf);
    if (AAC_DEC_OK != aacDecoder_ConfigRaw(ctx->handle, conf, &conf_len)) {
        LOGE("aacDecoder_ConfigRaw error");
        aacDecoder_Close(ctx->handle);
        free(ctx->pcm_buf);
        free(ctx);
        return NULL;
    }

    return ctx;
}

void aac_dec_close(aac_dec_ctx* ctx) {
    aacDecoder_Close(ctx->handle);
    free(ctx->pcm_buf);
    free(ctx);
}

void aac_dec_decode(aac_dec_ctx* ctx, uint8_t* buf, uint buf_size) {
    UCHAR* input_buf[] = { buf };
    UINT valid_size = buf_size;

    memset(ctx->pcm_buf, 0, PCM_BUF_SIZE);

    if (AAC_DEC_OK != aacDecoder_Fill(ctx->handle, input_buf, &buf_size, &valid_size)) {
        LOGE("aacDecoder_Fill error");
        return;
    }

    if (AAC_DEC_OK != aacDecoder_DecodeFrame(ctx->handle, ctx->pcm_buf, PCM_BUF_SIZE, 0)) {
        LOGE("aacDecoder_DecodeFrame error");
        return;
    }
}
