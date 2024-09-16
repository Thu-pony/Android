package com.example.playaudiotest

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    val mediaPlayer = MediaPlayer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initMediaPlayer()
        val play_btn = findViewById<Button>(R.id.play)
        play_btn.setOnClickListener {
            if (!mediaPlayer.isPlaying)
                mediaPlayer.start()
        }
        val pause_btn = findViewById<Button>(R.id.pause)
        pause_btn.setOnClickListener {
            if (mediaPlayer.isPlaying)
                mediaPlayer.pause()
        }
        val stop_btn = findViewById<Button>(R.id.stop)
        stop_btn.setOnClickListener {
            if (!mediaPlayer.isPlaying)
                mediaPlayer.reset()
                initMediaPlayer()
        }
    }
    private  fun initMediaPlayer() {
        val assetManger = assets
        val fd = assetManger.openFd("TEST01.mp3")
        mediaPlayer.setDataSource(fd.fileDescriptor,fd.startOffset,fd.length)
        mediaPlayer.prepare()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()
        mediaPlayer.release()
    }

}