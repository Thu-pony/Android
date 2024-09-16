package com.example.file.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class MyDatabastHelper(val context: Context, name:String, version:Int) : SQLiteOpenHelper(context,name,null,version){
    private val createBook = "create table Book (" +
                " id integer primary key autoincrement," +
                "author text," +
                "price real," +
                "pages integer," +
                "name text)"
    private val sql1 = "create table Category (id integer primary key autoincrement,category_name text,category_code integer)"

    override fun onCreate(db: SQLiteDatabase?) {
        //Toast.makeText(context,"onCreate",Toast.LENGTH_LONG).show()
        Log.d("Provider","onCreate")
        db?.execSQL(createBook)
        db?.execSQL(sql1)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        //Toast.makeText(context,"onUpgrade",Toast.LENGTH_LONG).show()
        Log.d("Provider","onupgrade")
        db?.execSQL(createBook)
        db?.execSQL(sql1)
    }
}