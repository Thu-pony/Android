package com.example.file

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.file.util.MyDatabastHelper

class SqlActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sql)
        val btn_create = findViewById<Button>(R.id.create)
        val add = findViewById<Button>(R.id.add)
        val databastHelper = MyDatabastHelper(this,"book.db",3)
        btn_create.setOnClickListener {
            databastHelper.writableDatabase
        }
        add.setOnClickListener {
            val db = databastHelper.writableDatabase
            val contenvals = ContentValues().apply {
                put("author","pony")
                put("name", "lol_t1")
                put("pages",111)
                put("price",110.0)
            }
            db.insert("Book",null,contenvals)
            val contenvals1 = ContentValues().apply {
                put("author","Apony")
                put("name", "Alol_t1")
                put("pages",101)
                put("price",1220.0)
            }
            db.insert("Book",null,contenvals1)

        }
    }
}