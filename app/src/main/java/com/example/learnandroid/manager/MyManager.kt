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

    // private val mFragment = MyFragment()

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