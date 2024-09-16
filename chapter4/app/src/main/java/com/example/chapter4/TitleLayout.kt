package com.example.chapter4

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import java.util.jar.Attributes

class TitleLayout(context: Context, attributes: AttributeSet):LinearLayout(context,attributes) {
    init {
        LayoutInflater.from(context).inflate(R.layout.title, this)
        val back:Button = findViewById(R.id.titleBack)
        back.setOnClickListener {
            Toast.makeText(context,"back",Toast.LENGTH_LONG).show()
        }
        val edit:Button = findViewById(R.id.titleEdit)
        edit.setOnClickListener {
            Toast.makeText(context,"edit" ,Toast.LENGTH_LONG).show()
        }

    }
}