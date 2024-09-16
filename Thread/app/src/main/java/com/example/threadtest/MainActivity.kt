package com.example.threadtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Button
import android.widget.TextView
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    val updateText = 1
    val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                updateText -> {
                    val text = findViewById<TextView>(R.id.textView)
                    text.text = "Nice to meet you"
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.changeTextBtn)
        btn.setOnClickListener {
            thread { val msg = Message()
            msg.what = updateText
            handler.sendMessage(msg)}
        }
    }
}