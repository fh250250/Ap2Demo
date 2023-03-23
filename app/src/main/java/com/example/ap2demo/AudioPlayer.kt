package com.example.ap2demo

import android.util.Log
import com.github.serezhka.airplay.lib.AudioStreamInfo

class AudioPlayer {
    private val TAG = "AudioPlayer"

    private val packetList = mutableListOf<ByteArray>()
    private val packetLimit = 10

    fun start(audioStreamInfo: AudioStreamInfo) {
        Log.i(TAG, "start $audioStreamInfo")
    }

    fun stop() {
        Log.i(TAG, "stop")
    }

    fun addPacket(packet: ByteArray) {
        if (packetList.size >= packetLimit) packetList.removeFirstOrNull()
        packetList.add(packet)
    }
}