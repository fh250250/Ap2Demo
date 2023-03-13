package com.github.fh250250.ap2demo

import android.media.MediaCodec
import android.media.MediaFormat
import android.util.Log
import android.view.SurfaceView
import java.nio.ByteBuffer
import java.util.*
import kotlin.concurrent.thread

class VideoPlayer(val surfaceView: SurfaceView) {
    private val TAG = "VideoPlayer"

    private val codec: MediaCodec
    private val packetList = Collections.synchronizedList(ArrayList<ByteArray>())
    private var isEnd = false

    init {
        val mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, surfaceView.width, surfaceView.height)
        codec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        codec.setVideoScalingMode(MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
        codec.configure(mediaFormat, surfaceView.holder.surface, null, 0)
        codec.start()

        thread { mainLoop() }
    }

    fun addPacket(packet: ByteArray) {
        packetList.add(packet)
    }

    private fun mainLoop() {
        while (!isEnd) {
            if (packetList.size == 0) {
                Thread.sleep(50)
                continue
            }
            decode(packetList.removeAt(0))
        }
    }

    private fun decode(packet: ByteArray) {
        val TIMEOUT_USEC = 10_000L
        val decoderInputBuffers: Array<ByteBuffer> = codec.getInputBuffers()
        var inputBufIndex = -10000
        try {
            inputBufIndex = codec.dequeueInputBuffer(TIMEOUT_USEC)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (inputBufIndex >= 0) {
            val inputBuf: ByteBuffer = decoderInputBuffers[inputBufIndex]
            inputBuf.put(packet)
            codec.queueInputBuffer(inputBufIndex, 0, packet.size, 0, 0)
        } else {
            Log.d(TAG, "dequeueInputBuffer failed")
        }

        var outputBufferIndex = -10000
        try {
            val mBufferInfo = MediaCodec.BufferInfo()
            outputBufferIndex = codec.dequeueOutputBuffer(mBufferInfo, TIMEOUT_USEC)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (outputBufferIndex >= 0) {
            codec.releaseOutputBuffer(outputBufferIndex, true)
            try {
                Thread.sleep(50)
            } catch (ie: InterruptedException) {
                ie.printStackTrace()
            }
        } else if (outputBufferIndex == MediaCodec.INFO_TRY_AGAIN_LATER) {
            try {
                Thread.sleep(10)
            } catch (ie: InterruptedException) {
                ie.printStackTrace()
            }
        } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
            // not important for us, since we're using Surface
        } else if (outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
        } else {
        }
    }
}