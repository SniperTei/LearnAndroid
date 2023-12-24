package com.example.learnandroid.bean

class WanResponseBean<T> {
    val errorCode: Int? = null
    val errorMsg: String? = null
    val data: T? = null
    override fun toString(): String {
        return "WanResponseBean(errorCode=$errorCode, errorMsg=$errorMsg, data=$data)"
    }
}