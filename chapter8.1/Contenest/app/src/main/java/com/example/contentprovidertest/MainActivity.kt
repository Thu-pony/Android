package com.example.contentprovidertest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.ContactsContract
import android.provider.ContactsContract.Contacts
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.contentValuesOf

class MainActivity : AppCompatActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val add_data = findViewById<Button>(R.id.addData)
        add_data.setOnClickListener {
            val uri = Uri.parse("content://com.example.file.provider/book")
            val values = contentValuesOf("name" to "A","author" to "B","pages" to 101, "price" to 22.8)
            val newUri = contentResolver.insert(uri,values)
        }
        val query_data = findViewById<Button>(R.id.queryData)
        query_data.setOnClickListener {
            val uri = Uri.parse("content://com.example.file.provider/book")
            contentResolver.query(uri,null,null,null,null)?.apply {
                while (moveToNext()) {
                    Log.d("Provider","book name is ${getString(getColumnIndex("name"))}")
                    Log.d("Provider","book author is ${getString(getColumnIndex("author"))}")
                    Log.d("Provider","book pages is ${getInt(getColumnIndex("pages"))}")
                    Log.d("Provider","book price is ${getInt(getColumnIndex("price"))}")
                }
            }
        }
//        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),1)
//        }
//        else{
//            initData()
//        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)initData()
                else Toast.makeText(this,"权限拒绝",Toast.LENGTH_LONG).show()
            }
        }

    }
    @SuppressLint("Range")
    fun initData() {
        //查询数据
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)?.apply {
            while (moveToNext()) {
                val name = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                Log.d("Providr",name)
                val number = getString(getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                Log.d("Providr",number)
            }
        }

    }
}