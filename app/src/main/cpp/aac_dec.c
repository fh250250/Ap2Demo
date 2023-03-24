#include "aac_dec.h"

HANDLE_AACDECODER aac_dec_open() {

    HANDLE_AACDECODER handle;

    handle = aacDecoder_Open(TT_MP4_RAW, 1);
    if (handle == NULL) {
        LOGE("aacDecoder_Open");
        return NULL;
    }

    UCHAR eld_conf[] = { 0xF8, 0xE8, 0x50, 0x00 };
    UCHAR *conf[] = { eld_conf };
    UINT conf_len = sizeof(eld_conf);
    if (AAC_DEC_OK != aacDecoder_ConfigRaw(handle, conf, &conf_len)) {
        LOGE("aacDecoder_ConfigRaw");
        aacDecoder_Close(handle);
        return NULL;
    }

    return handle;
}

void aac_dec_close(HANDLE_AACDECODER handle) {
    aacDecoder_Close(handle);
}

void aac_dec_decode(HANDLE_AACDECODER handle) {
//    aacDecoder_Fill(handle, );
//    aacDecoder_DecodeFrame(handle, );
}
