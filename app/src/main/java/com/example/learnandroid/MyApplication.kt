package com.example.learnandroid

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log

class MyApplication: Application() {

    private val TAG = "MyApplication"

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "MyApplication onCreate")
    }

    override fun onTerminate() {
        super.onTerminate()
        Log.i(TAG, "MyApplication onTerminate")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        Log.i(TAG, "MyApplication onLowMemory")
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.i(TAG, "MyApplication onTrimMemory")
    }

    override fun onConfigurationChanged(newConfig: android.content.res.Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i(TAG, "MyApplication onConfigurationChanged")
    }

    override fun registerActivityLifecycleCallbacks(callback: android.app.Application.ActivityLifecycleCallbacks) {
        super.registerActivityLifecycleCallbacks(callback)
        Log.i(TAG, "MyApplication registerActivityLifecycleCallbacks")
    }

    override fun unregisterActivityLifecycleCallbacks(callback: android.app.Application.ActivityLifecycleCallbacks) {
        super.unregisterActivityLifecycleCallbacks(callback)
        Log.i(TAG, "MyApplication unregisterActivityLifecycleCallbacks")
    }

    override fun registerComponentCallbacks(callback: android.content.ComponentCallbacks) {
        super.registerComponentCallbacks(callback)
        Log.i(TAG, "MyApplication registerComponentCallbacks")
    }
}