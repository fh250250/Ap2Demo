package com.example.ap2demo

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import com.github.serezhka.airplay.lib.AudioStreamInfo
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class AudioPlayer {
    private val TAG = "AudioPlayer"

    private lateinit var audioTrack: AudioTrack
    private val packetList = LinkedBlockingQueue<ByteArray>()
    private val packetLimit = 10
    private var aacPtr = 0L
    private var loopEnd = true

    fun start(audioStreamInfo: AudioStreamInfo) {
        Log.i(TAG, "start $audioStreamInfo")

        if (aacPtr == 0L) aacPtr = aacOpen()
        if (aacPtr == 0L) return
        initAudioTrack()
        audioTrack.play()
        loopEnd = false
        thread { loop() }
    }

    fun stop() {
        Log.i(TAG, "stop")

        loopEnd = true
        if (aacPtr != 0L) {
            aacClose(aacPtr)
            aacPtr = 0L
        }
        try {
            audioTrack.flush()
            audioTrack.stop()
            audioTrack.release()
        } catch (_: Exception) {}
    }

    fun addPacket(packet: ByteArray) {
        if (packetList.size >= packetLimit) packetList.poll()
        packetList.offer(packet)
    }

    private fun loop() {
        while (!loopEnd) {
            val packet = packetList.poll(10, TimeUnit.MILLISECONDS) ?: continue
            val pcm = aacDecode(aacPtr, packet)

            audioTrack.write(pcm, 0, pcm.size)
        }
    }

    private fun initAudioTrack() {
        val sampleRate = 44100
        val channel = AudioFormat.CHANNEL_OUT_STEREO
        val encoding = AudioFormat.ENCODING_PCM_16BIT
        val minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channel, encoding)
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        val audioFormat = AudioFormat.Builder()
            .setSampleRate(sampleRate)
            .setEncoding(encoding)
            .setChannelMask(channel)
            .build()

        audioTrack = AudioTrack(audioAttributes, audioFormat, minBufferSize, AudioTrack.MODE_STREAM, AudioManager.AUDIO_SESSION_ID_GENERATE)
    }

    private external fun aacOpen(): Long
    private external fun aacClose(ptr: Long)
    private external fun aacDecode(ptr: Long, buf: ByteArray): ShortArray

    companion object {
        init {
            System.loadLibrary("jni")
        }
    }
}