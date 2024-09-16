package com.example.webviewtest

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class MainActivity : ComponentActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activety_main)
    //    val webview = findViewById<WebView>(R.id.webView)
      //  webview.settings.javaScriptEnabled = true
        //webview.webViewClient = WebViewClient()
        //webview.loadUrl("https://www.google.com")
        val btn = findViewById<Button>(R.id.sendRequestBtn)
   btn.setOnClickListener {
       sendRequset()
   }
//            thread {
//                val uri = URL(https://www.google.com
//                val con = uri.openConnection() as HttpURLConnection
//                val response = StringBuilder()
//                con.requestMethod = "POST"
//                val output = DataOutputStream(con.outputStream)
//                output.writeBytes("username=admin&password=123456")
//                con.connectTimeout = 8000
//                con.readTimeout = 8000
//                val input = con.inputStream
//                val reader = BufferedReader(InputStreamReader(input))
//                reader.use {
//                    reader.forEachLine {
//                        response.append(it)
//                    }
//                }
//                val show = findViewById<TextView>(R.id.responseText)
//                runOnUiThread { show.text = response }
//
//                con.disconnect()
//            }
//
//        }

    }
    fun sendRequset() {
        thread {
            val client = OkHttpClient()
            val requset = Request.Builder()
                .url("https://www.baidu.com")
                .build()
            val response = client.newCall(requset).execute()
            val Data = response.body?.string()
            val show = findViewById<TextView>(R.id.responseText)
            runOnUiThread { show.text = Data }
        }

    }


}