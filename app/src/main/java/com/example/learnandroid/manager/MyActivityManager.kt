package com.example.learnandroid.manager

import android.app.Activity
import java.util.Stack

class MyActivityManager {
    companion object {
        private var instance: MyActivityManager? = null
        // singleton
        fun getInstance(): MyActivityManager {
            return instance ?: synchronized(this) { 
                instance ?: MyActivityManager().also { instance = it }
             }
        }
    }

    private val activityStack: Stack<Activity> = Stack<Activity>()

    // 添加activity
    fun addActivity(activity: Activity) {
        activityStack.add(activity)
    }

    // 移除activity
    fun removeActivity(activity: Activity) {
        activityStack.remove(activity)
    }

    // 获取当前展示的activity
    fun getCurrentActivity(): Activity {
        return activityStack.lastElement()
    }

    // 结束当前展示的activity
    fun finishCurrentActivity() {
        val activity = activityStack.lastElement()
        finishActivity(activity)
    }

    // 结束指定的activity
    fun finishActivity(activity: Activity) {
        activityStack.remove(activity)
        activity.finish()
    }

    // 结束指定类名的activity
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    // 结束所有的activity
    fun finishAllActivity() {
        for (activity in activityStack) {
            activity.finish()
        }
        activityStack.clear()
    }

    // 退出应用程序
    fun exitApp() {
        finishAllActivity()
        System.exit(0)
    }
}