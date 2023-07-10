package com.example.learnandroid.manager

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.example.learnandroid.R
import com.example.learnandroid.fragment.MyFragment

class MyManager private constructor() {
    companion object {
        private var instance: MyManager? = null
        // singleton
        fun getInstance(): MyManager {
            return instance ?: synchronized(this) { 
                instance ?: MyManager().also { instance = it }
             }
        }
    }

    private val TAG = "MyManager"

    private val mFragment = MyFragment()

    // 把mFragment显示在当前activity中
    fun showFragment() {
        Log.i(TAG, "myFragment = $mFragment")
        // fragmentManager
        // 通过Activity获取FragmentManager
        val activity = getCurrentActivity()
        Log.i(TAG, "activity = $activity")
        // 把mFragment添加到当前activity中
        // 通过Context获取FragmentManager
        val context = activity?.applicationContext
        Log.i(TAG, "context = $context")
        // 通过Activity获取FragmentManager
        activity
        
        
        
        // val fragmentManager = activity?.supportFragmentManager
        // Log.i(TAG, "fragmentManager = $fragmentManager")
        // // 开启一个事务
        // val transaction = fragmentManager?.beginTransaction()
        // Log.i(TAG, "transaction = $transaction")
        // // 将fragment添加到布局中
        // transaction?.add(R.id.fl_fragment_container, mFragment)
        // // 提交事务
        // transaction?.commit()

        // val fragmentManager = supportFragmentManager
        // Log.i(TAG, "fragmentManager = $fragmentManager")
        // // 开启一个事务
        // val transaction = fragmentManager.beginTransaction()
        // Log.i(TAG, "transaction = $transaction")
        // // 将fragment添加到布局中
        // transaction.add(R.id.fl_fragment_container, mFragment!!)
        // // 提交事务
        // transaction.commit()
    }

    // 把mFragment从当前activity中移除
    fun removeFragment() {
        Log.i(TAG, "removeFragment")
    }


    // 获取当前展示的activity
    fun getCurrentActivity(): Activity? {
        Log.i(TAG, "getCurrentActivity")
        // 获取当前展示的activity
        val activityManager = MyActivityManager.getInstance()
        val currentActivity = activityManager.getCurrentActivity()
        Log.i(TAG, "currentActivity = $currentActivity")
        return currentActivity
    }

    fun doSomething() {
        println("doSomething")
    }

    fun doSomething2() {
        println("doSomething2")
    }

    fun doSomething3() {
        println("doSomething3")
    }
}