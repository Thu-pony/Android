package com.example.jetpacktest


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap


class MainViewmodel(conteReserved:Int) :ViewModel() {

    val counter = MutableLiveData<Int>()
    init {
        counter.value = conteReserved
    }
    val userDat = MutableLiveData<User>()
    val userData :LiveData<String> = userDat.map {
        user -> "${user.firstName} ${user.lastName}"
    }

//    val userID: LiveData<User> = userDat.switchMap {userID -> Repository.Repository.getUser(userID)
//    }
    //计数器功能
    var counte = conteReserved
    fun plusOne() {
        val conunt = counter.value?:0
        counter.value = conunt + 1
    }
    fun clera() {
        counter.value = 0
    }

}