package com.example.sunnyweather.logic

import com.example.sunnyweather.SunnyWeatherApplication
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {
    @GET("key=${SunnyWeatherApplication.APIKEY}&?&aqi=no")
    fun searchPlaces(@Query("q") q: String): Call<PlaceResponse>
}