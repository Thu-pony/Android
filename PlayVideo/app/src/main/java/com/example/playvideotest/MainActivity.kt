package com.example.playvideotest

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.VideoView

class MainActivity : AppCompatActivity() {
    lateinit var videoview : VideoView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val uri = Uri.parse("android.resource://$packageName/${R.raw.trailer}")
        videoview = findViewById<VideoView>(R.id.videoView)
        videoview.setVideoURI(uri)
        val play_btn = findViewById<Button>(R.id.play)
        play_btn.setOnClickListener {
            if (!videoview.isPlaying)
                videoview.start()
        }
        val pause_btn = findViewById<Button>(R.id.pause)
        pause_btn.setOnClickListener {
            if (videoview.isPlaying)
                videoview.pause()
        }
        val replay_btn = findViewById<Button>(R.id.replay)
        replay_btn.setOnClickListener {
            if (videoview.isPlaying)
                videoview.resume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoview.suspend()
    }
}