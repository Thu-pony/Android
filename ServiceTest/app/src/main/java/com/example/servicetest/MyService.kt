package com.example.servicetest

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.os.Parcel
import android.util.Log

const val TAG = "myserive"
class MyService : Service() {
    private val mBinder = DownLoadBinder()

    class DownLoadBinder : Binder() {
        fun startDownLoad() {
            Log.d(TAG,"startDownLoad")
        }
        fun onProcess() {
            Log.d(TAG,"onProcess")
        }
        fun stopDownLoad() {
            Log.d(TAG,"stopDownLoad")
        }

    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG,"onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
    }
    override fun onBind(intent: Intent): IBinder {
        Log.d(TAG,"onBind-")
        return mBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG,"onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

}