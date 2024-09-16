package com.example.file

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter


class MainActivity : AppCompatActivity() {

    fun SharedPreferences.open(block: SharedPreferences.Editor.() -> Unit) {
        val  editor = edit()
        editor.block()
        editor.apply()
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn = findViewById<Button>(R.id.save_sf)
//        btn.setOnClickListener {
//            //名称
////            val editor = getSharedPreferences("data",Context.MODE_PRIVATE).edit()
////            editor.putString("name","pony")
////            editor.putInt("age",25)
////            editor.putBoolean("married",false)
////            editor.apply()
//            getSharedPreferences("data",Context.MODE_PRIVATE).open {
//                putString("name","zzzz")
//                putInt("age",28)
//                putBoolean("married",true)
//            }
//        }
//        val btn1 = findViewById<Button>(R.id.load_sf)
//        btn1.setOnClickListener { showsf() }
//        val inputText = findViewById<EditText>(R.id.input)
//        val input = load()
//        if (input.isNotEmpty()) {
//            inputText.setText(input)
//            inputText.setSelection(input.length)
//
//        }

    }
    fun showsf() {
        val sf = getSharedPreferences("data",Context.MODE_PRIVATE)
        val sb = "姓名是 " + sf.getString("name","") + "年龄是 " + sf.getInt("age",0) + "已婚否： " + sf.getBoolean("married",false)
        Toast.makeText(applicationContext,sb,Toast.LENGTH_LONG).show()
    }
    fun save(inputText: String) {
            val outputStream = openFileOutput("data", Context.MODE_PRIVATE)
            val writer = BufferedWriter(OutputStreamWriter(outputStream))
            writer.use {
                it.write(inputText)
            }
    }
    fun load():String{
        val content = StringBuilder()
        content.apply {
            val input = openFileInput("data")
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine { this.append(it) }
            }
        }
        return content.toString()
    }
    override fun onDestroy() {
        super.onDestroy()
        val inputText = findViewById<EditText>(R.id.input)
        val inputString = inputText.text.toString()
        save(inputString)
    }

}