package com.example.test

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {
    lateinit var offlinereceiver:OfflineReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
    }

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter()
        intentFilter.addAction("force_offline")
        offlinereceiver = OfflineReceiver()
        registerReceiver(offlinereceiver,intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(offlinereceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }
    inner class OfflineReceiver:BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            AlertDialog.Builder(context).apply { setTitle("Warning")
            setMessage("你的账号被异地登录")
            setCancelable(false)
            setPositiveButton("OK") {
                _,_ ->
                ActivityCollector.finishAll()
                val i  = Intent(context,LoginActivity::class.java)
                context?.startActivity(i)
            }.show()
            }

        }
    }

}