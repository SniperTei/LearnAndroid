package com.example.learnandroid.bean

class ErrorBean {
    val errorCode: Int
    val errorMsg: String

    constructor(errorCode: Int, errorMsg: String) {
        this.errorCode = errorCode
        this.errorMsg = errorMsg
    }
}