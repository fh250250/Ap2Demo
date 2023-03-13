package com.github.fh250250.ap2demo

import android.os.Build
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.github.fh250250.ap2.lib.AudioStreamInfo
import com.github.fh250250.ap2.lib.VideoStreamInfo
import com.github.fh250250.ap2.server.AirPlayConfig
import com.github.fh250250.ap2.server.AirPlayConsumer
import com.github.fh250250.ap2.server.AirPlayServer
import kotlin.concurrent.thread

class Server(val surfaceView: SurfaceView) : SurfaceHolder.Callback, AirPlayConsumer {
    private val TAG = "Server"

    lateinit var airPlayServer: AirPlayServer
    lateinit var videoPlayer: VideoPlayer
    private val fps = 25

    init {
        surfaceView.holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.i(TAG, "surfaceCreated")

        val airPlayConfig = AirPlayConfig()
        airPlayConfig.serverName = Build.MODEL
        airPlayConfig.width = surfaceView.width
        airPlayConfig.height = surfaceView.height
        airPlayConfig.fps = fps
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

    override fun onVideoFormat(videoStreamInfo: VideoStreamInfo?) {
        Log.i(TAG, "onVideoFormat")
    }

    override fun onVideo(bytes: ByteArray?) {
        Log.i(TAG, "onVideo")
        bytes?.let { videoPlayer.addPacket(it) }
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