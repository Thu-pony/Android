package com.example.threadtest

import android.os.AsyncTask
import android.util.Log
import android.widget.Toast

class DownTask:AsyncTask<Unit,Int,Boolean> {
    var downloadPercent = 1
    @Deprecated("Deprecated in Java")
    override fun onPreExecute() {
        Toast.makeText(this,"开始下载",Toast.LENGTH_LONG).show()
    }
    override fun doInBackground(vararg params: Unit?): Boolean {
        while (true) {
            Thread.sleep(1000)
            downloadPercent++
            publishProgress(downloadPercent)
            if (downloadPercent >= 100) {
                break
            }
        }
        return  true
    }

    override fun onProgressUpdate(vararg values: Int?) {
        Log.d("aaa", "下载进度 ${values}")

    }
}