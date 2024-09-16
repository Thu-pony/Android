package com.example.test

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.test.ui.theme.TestTheme

class MainActivity : BaseActivity() {
    lateinit var  TimeChageReceiver: TimeChangeReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.intent.action.TIME_TICK")
        TimeChageReceiver = TimeChangeReceiver();
        // 动态注册
        //registerReceiver(TimeChageReceiver,intentFilter)
        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener { val intent = Intent("force_offline")
            intent.setPackage(packageName)
        sendBroadcast(intent)}
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(TimeChageReceiver)
    }
    inner class TimeChangeReceiver:BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Toast.makeText(context,"Time has changed", Toast.LENGTH_LONG).show();
        }
    }
}

