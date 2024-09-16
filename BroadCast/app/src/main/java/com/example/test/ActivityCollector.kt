package com.example.test

import android.app.Activity

object ActivityCollector {
    private val activites = ArrayList<Activity>()
    fun addActivity(activity: Activity) {
        activites.add(activity)
    }
    fun removeActivity(activity: Activity) {
        activites.remove(activity)
    }
    fun finishAll() {
        for (activity in activites) {
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
        activites.clear()
    }

}