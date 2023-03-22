package com.github.fh250250.ap2demo

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import com.github.fh250250.ap2.lib.AudioStreamInfo
import com.github.fh250250.ap2.lib.VideoStreamInfo
import com.github.fh250250.ap2.server.AirPlayConfig
import com.github.fh250250.ap2.server.AirPlayConsumer
import com.github.fh250250.ap2.server.AirPlayServer
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback, AirPlayConsumer {
    private val TAG = "ap2demo"

    private lateinit var surfaceView: SurfaceView
    private lateinit var airPlayServer: AirPlayServer
    private lateinit var videoPlayer: VideoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        surfaceView = findViewById(R.id.surfaceView)
        surfaceView.holder.addCallback(this)
    }

    override fun onVideoFormat(videoStreamInfo: VideoStreamInfo?) {
        videoPlayer.start()
    }

    override fun onVideo(bytes: ByteArray?) {
        bytes?.let { videoPlayer.addPacket(it) }
    }

    override fun onVideoSrcDisconnect() {
        videoPlayer.stop()
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

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.i(TAG, "surfaceCreated size=${surfaceView.width}x${surfaceView.height}")

        val airPlayConfig = AirPlayConfig()
        airPlayConfig.serverName = Build.MODEL
        airPlayConfig.width = surfaceView.width
        airPlayConfig.height = surfaceView.height
        airPlayConfig.fps = 25
        airPlayServer = AirPlayServer(airPlayConfig, this)
        thread { airPlayServer.start() }

        videoPlayer = VideoPlayer(surfaceView)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.i(TAG, "surfaceChanged")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.i(TAG, "surfaceDestroyed")
    }
}