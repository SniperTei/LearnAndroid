package com.example.learnandroid.app.data.bean

import com.example.common_module.network.BaseNetworkResponse

data class AppResponse<T>(val errorCode: Int, val errorMsg: String, val data: T): BaseNetworkResponse<T>() {
    override fun isResponseSuccess(): Boolean {
        return errorCode == 0
    }

    override fun getResponseCode(): Int {
        return errorCode
    }

    override fun getResponseMsg(): String {
        return errorMsg
    }

    override fun getResponseData(): T {
        return data
    }
}
