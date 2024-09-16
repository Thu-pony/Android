package com.example.fragmenttest

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btn:Button = findViewById(R.id.button)
        btn.setOnClickListener {
            replaceFragment(right_Fragment())
        }
      //  replaceFragment(right_Fragment())
 //       val Left_Fragment = supportFragmentManager.findFragmentById(R.id.rightFrag) as Left_Fragment
    }
    private  fun replaceFragment(fragment: Fragment) {
        val fragmentStateManager = supportFragmentManager
        val transition = fragmentStateManager.beginTransaction()
        right_Fragment.conut++
        transition.replace(R.id.rightFrag,fragment)
       // transition.addToBackStack(null)
        transition.commit()
    }
}