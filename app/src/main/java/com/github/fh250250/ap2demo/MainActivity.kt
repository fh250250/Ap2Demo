package com.github.fh250250.ap2demo

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.serezhka.airplay.lib.AudioStreamInfo
import com.github.serezhka.airplay.lib.VideoStreamInfo
import com.github.serezhka.airplay.server.AirPlayConfig
import com.github.serezhka.airplay.server.AirPlayConsumer
import com.github.serezhka.airplay.server.AirPlayServer
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), SurfaceHolder.Callback, AirPlayConsumer {
    private val TAG = "ap2demo"

    private lateinit var playerView: RelativeLayout
    private lateinit var surfaceView: SurfaceView
    private lateinit var infoView: TextView
    private lateinit var airPlayServer: AirPlayServer
    private lateinit var videoPlayer: VideoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.playerView)
        surfaceView = findViewById(R.id.surfaceView)
        surfaceView.holder.addCallback(this)

        infoView = findViewById(R.id.infoView)
        infoView.text = "设备 [${Build.MODEL}] 等待连接"
    }

    override fun onVideoFormat(videoStreamInfo: VideoStreamInfo?) {
        runOnUiThread { infoView.visibility = View.GONE }
        videoPlayer.start()
    }

    override fun onVideo(bytes: ByteArray?) {
        bytes?.let { videoPlayer.addPacket(it) }
    }

    override fun onVideoSrcDisconnect() {
        runOnUiThread { infoView.visibility = View.VISIBLE }
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

        airPlayServer = AirPlayServer(
            AirPlayConfig().apply {
                serverName = Build.MODEL
                width = playerView.width
                height = playerView.height
                fps = 25
            },
            this
        )
        thread { airPlayServer.start() }

        videoPlayer = VideoPlayer(surfaceView, width = playerView.width, height = playerView.height)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.i(TAG, "surfaceChanged size=${width}x${height}")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.i(TAG, "surfaceDestroyed")
    }
}