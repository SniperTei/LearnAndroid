package com.example.learnandroid.manager

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

//    private val mFragment = MyFragment()

    // 把mFragment显示在当前activity中
    fun showFragment(containerId: Int) {
        print(message = "showFragment")
    }

    // 把mFragment从当前activity中移除
    fun removeFragment() {
        print(message = "removeFragment")
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