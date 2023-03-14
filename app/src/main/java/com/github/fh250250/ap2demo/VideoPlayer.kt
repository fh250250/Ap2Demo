package com.github.fh250250.ap2demo

import android.media.MediaCodec
import android.media.MediaFormat
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.util.concurrent.LinkedBlockingQueue

class VideoPlayer(private val surfaceView: SurfaceView) : SurfaceHolder.Callback {
    private val TAG = "VideoPlayer"

    private val packetList = LinkedBlockingQueue<ByteArray>()
    private lateinit var codec: MediaCodec

    init {
        surfaceView.holder.addCallback(this)
    }

    fun stop() {
        packetList.clear()
    }

    fun addPacket(packet: ByteArray) {
        packetList.add(packet)
    }

    private fun setupCodec() {
        val mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, surfaceView.width, surfaceView.height)
        codec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        codec.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT)
        codec.setCallback(object : MediaCodec.Callback() {
            override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
                val packet = packetList.take()
                val buffer = codec.getInputBuffer(index)

                buffer?.put(packet)
                codec.queueInputBuffer(index, 0, packet.size, 0, 0)
            }

            override fun onOutputBufferAvailable(
                codec: MediaCodec,
                index: Int,
                info: MediaCodec.BufferInfo
            ) {
                codec.releaseOutputBuffer(index, true)
            }

            override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
                Log.e(TAG, "onError", e)
            }

            override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
                Log.i(TAG, "onOutputFormatChanged")
            }

        })
        codec.configure(mediaFormat, surfaceView.holder.surface, null, 0)
        codec.start()
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        setupCodec()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        codec.stop()
        codec.release()
        setupCodec()
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        codec.stop()
        codec.release()
    }
}