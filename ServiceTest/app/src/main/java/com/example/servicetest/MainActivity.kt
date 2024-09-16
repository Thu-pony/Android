package com.example.servicetest

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Button
inline fun <reified  T> startService(context:Context) {
    val intent = Intent(context,T::class.java)
    context.startService(intent)
}
class MainActivity : AppCompatActivity() {

    lateinit var downLoadbinder : MyService.DownLoadBinder
    private val conncetion = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            downLoadbinder = service as MyService.DownLoadBinder
            downLoadbinder.startDownLoad()
            downLoadbinder.onProcess()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d(TAG,"onDisConnected")
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn_start = findViewById<Button>(R.id.startServiceBtn)
        Log.d("serevice" ," main ${Thread.currentThread().name}")
        btn_start.setOnClickListener {
            //val intent = Intent(this,MyIntentService::class.java)
            //val intent = Intent(this,MyService::class.java)
            //startService(intent)
            //bindService(intent,conncetion,Context.BIND_AUTO_CREATE)
            startService<MyIntentService>(this)
        }
        val btn_stop = findViewById<Button>(R.id.stopServiceBtn)
        btn_stop.setOnClickListener {
            val intent = Intent(this,MyService::class.java)
            //stopService(intent)
            unbindService(conncetion)
        }
    }
}