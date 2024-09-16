package com.example.sunnyweather

import android.app.Application
import android.content.Context

class SunnyWeatherApplication: Application() {
    companion object{
        const val APIKEY = "a91bcb46c5f44bd2a8171229240208"
        lateinit var context:Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}