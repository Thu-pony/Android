package com.example.sunnyweather.logic

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import java.lang.Exception


object Repository {
    fun searchPlaces(q:String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeRespnse = SunnyWeatherNetwork.searchPlaces(q)
            if (placeRespnse.status == "ok")
            {
                val places = placeRespnse.places
                Result.success(places)
            }else {
                Result.failure(RuntimeException("response status is ${placeRespnse.status}"))
            }
            }catch (e:Exception){
                Result.failure<List<Place>>(e)
        }
        emit(result)
    }
}