package com.example.jetpacktest

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    lateinit var viewmodel: MainViewmodel
    lateinit var sp:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
            .setInitialDelay(5000,TimeUnit.MILLISECONDS)
            .addTag("simple")
            .build()
        WorkManager.getInstance(this).enqueue(request)
        WorkManager.getInstance(this).cancelAllWorkByTag("simple")
        val update = findViewById<Button>(R.id.updateDataBtn)
        val adddate = findViewById<Button>(R.id.addDataBtn)
//        val userDao = AppDatabase.getDatabase(this).UserDao()
//        val user1 = User("11","22",10)
//        val user2 = User("33","44",11)
//        adddate.setOnClickListener {
//            thread {
//                user1.id = userDao.Insert(user1)
//             //   user2.id = userDao.Insert(user1)
//                Log.d("main", "${user1.id}")
//            }
//        }

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()

    }


    override fun onPause() {
        super.onPause()
       //sp.edit().putInt("count_reversed",viewmodel.counter)
    }

    override fun onDestroy() {
        super.onDestroy()
        //sp.edit().putInt("count_reversed",viewmodel.counter)
    }
}