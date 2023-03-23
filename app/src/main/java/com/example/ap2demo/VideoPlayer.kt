package com.example.ap2demo

import android.media.MediaCodec
import android.media.MediaFormat
import android.util.Log
import android.view.SurfaceView

class VideoPlayer(private val surfaceView: SurfaceView) {
    private val TAG = "VideoPlayer"

    private lateinit var codec: MediaCodec
    private val packetList = mutableListOf<ByteArray>()
    private val packetLimit = 10

    fun start() {
        Log.i(TAG, "start")
        try {
            initCodec()
            codec.start()
        } catch (_: Exception) {}
    }

    fun stop() {
        Log.i(TAG, "stop")
        try {
            codec.stop()
            codec.release()
        } catch (_: Exception) {}
    }

    fun addPacket(packet: ByteArray) {
        if (packetList.size >= packetLimit) packetList.removeFirstOrNull()
        packetList.add(packet)
    }

    private fun initCodec() {
        codec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)

        codec.setCallback(object : MediaCodec.Callback() {
            override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
                val packet = packetList.removeFirstOrNull()

                try {
                    val buffer = codec.getInputBuffer(index)

                    if (packet != null && buffer != null) {
                        buffer.put(packet)
                        codec.queueInputBuffer(index, 0, packet.size, 0, 0)
                    } else {
                        codec.queueInputBuffer(index, 0, 0, 0, 0)
                    }
                } catch (_: Exception) {}
            }

            override fun onOutputBufferAvailable(codec: MediaCodec, index: Int, info: MediaCodec.BufferInfo) {
                try {
                    codec.releaseOutputBuffer(index, true)
                } catch (_: Exception) {}
            }

            override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
                Log.e(TAG, "onError", e)
            }

            override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
                Log.i(TAG, "onOutputFormatChanged size=${format.getInteger(MediaFormat.KEY_WIDTH)}x${format.getInteger(MediaFormat.KEY_HEIGHT)}")
                surfaceView.holder.setFixedSize(format.getInteger(MediaFormat.KEY_WIDTH), format.getInteger(MediaFormat.KEY_HEIGHT))
            }
        })

        val mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, surfaceView.width, surfaceView.height)
        codec.configure(mediaFormat, surfaceView.holder.surface, null, 0)
    }
}