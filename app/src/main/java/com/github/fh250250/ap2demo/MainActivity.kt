package com.github.fh250250.ap2demo

import android.graphics.SurfaceTexture
import android.media.MediaCodec
import android.media.MediaFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import com.github.fh250250.ap2.lib.AudioStreamInfo
import com.github.fh250250.ap2.lib.VideoStreamInfo
import com.github.fh250250.ap2.server.AirPlayConfig
import com.github.fh250250.ap2.server.AirPlayConsumer
import com.github.fh250250.ap2.server.AirPlayServer
import java.util.Queue
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), TextureView.SurfaceTextureListener, AirPlayConsumer {
    val TAG = "ap2demo"

    lateinit var textureView: TextureView
    lateinit var airPlayServer: AirPlayServer
    lateinit var mediaCodec: MediaCodec

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setup()
    }

    private fun setup() {
        textureView = findViewById(R.id.textureView)
        textureView.surfaceTextureListener = this
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        Log.i(TAG, "onSurfaceTextureAvailable size=${width}x${height}")

        val mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height)
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15)
        mediaCodec = MediaCodec.createDecoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        mediaCodec.configure(mediaFormat, Surface(surface), null, 0)
        mediaCodec.start()

        val airPlayConfig = AirPlayConfig()
        airPlayConfig.serverName = Build.MODEL
        airPlayConfig.width = width
        airPlayConfig.height = height
        airPlayConfig.fps = 24
        airPlayServer = AirPlayServer(airPlayConfig, this)
        thread { airPlayServer.start() }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        Log.i(TAG, "onSurfaceTextureSizeChanged")
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        Log.i(TAG, "onSurfaceTextureDestroyed")
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        Log.i(TAG, "onSurfaceTextureUpdated")
    }

    override fun onVideoFormat(videoStreamInfo: VideoStreamInfo?) {
        Log.i(TAG, "onVideoFormat")
    }

    override fun onVideo(bytes: ByteArray?) {
        Log.i(TAG, "onVideo")
    }

    override fun onVideoSrcDisconnect() {
        Log.i(TAG, "onVideoSrcDisconnect")
    }

    override fun onAudioFormat(audioStreamInfo: AudioStreamInfo?) {
        Log.i(TAG, "onAudioFormat")
    }

    override fun onAudio(bytes: ByteArray?) {
        Log.i(TAG, "onAudio")
    }

    override fun onAudioSrcDisconnect() {
        Log.i(TAG, "onAudioSrcDisconnect")
    }
}