package com.example.jetpacktest

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MyObserver(val lifecycle:Lifecycle): LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun adtivityStart() {
        Log.d("observer","start + ${lifecycle.currentState}")
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun adtivityStop() {
        Log.d("observer","stop + ${lifecycle.currentState}")
    }
}