package com.example.scancode

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button = findViewById<Button>(R.id.button)
        val textView = findViewById<TextView>(R.id.text_view)
        button.setOnClickListener {
            val scanner = GmsBarcodeScanning.getClient(applicationContext)
            scanner.startScan().addOnSuccessListener {
                val result = it.rawValue
                textView.text = result
            }
        }
    }

}