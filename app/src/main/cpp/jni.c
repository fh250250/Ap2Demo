#include "jni.h"
#include "aac_dec.h"

JNIEXPORT jlong JNICALL
Java_com_example_ap2demo_AudioPlayer_aacOpen(JNIEnv *env, jobject thiz) {
    return (jlong)aac_dec_open();
}

JNIEXPORT void JNICALL
Java_com_example_ap2demo_AudioPlayer_aacClose(JNIEnv *env, jobject thiz, jlong ptr) {
    aac_dec_close((aac_dec_ctx*)ptr);
}

JNIEXPORT jshortArray JNICALL
Java_com_example_ap2demo_AudioPlayer_aacDecode(JNIEnv *env, jobject thiz, jlong ptr,
                                               jbyteArray buf) {
    jbyte* in_buf = (*env)->GetByteArrayElements(env, buf, NULL);
    jsize in_buf_size = (*env)->GetArrayLength(env, buf);

    aac_dec_decode((aac_dec_ctx*)ptr, (uint8_t*)in_buf, in_buf_size);
    (*env)->ReleaseByteArrayElements(env, buf, in_buf, 0);

    jshortArray out_buf = (*env)->NewShortArray(env, PCM_BUF_SIZE / 2);
    (*env)->SetShortArrayRegion(env, out_buf, 0, PCM_BUF_SIZE / 2, ((aac_dec_ctx*)ptr)->pcm_buf);

    return out_buf;
}