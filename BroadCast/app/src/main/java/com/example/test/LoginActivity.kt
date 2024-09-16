package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity :BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activitiy_login)
        val usr = findViewById<EditText>(R.id.accountEdit)
        val pwd = findViewById<EditText>(R.id.passwordEdit)
        val login = findViewById<Button>(R.id.login)
        login.setOnClickListener {
            if (usr.text.toString() == "admin" && pwd.text.toString() == "123456") {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Toast.makeText(this,"账号密码错误",Toast.LENGTH_LONG).show()
            }
        }

    }
}