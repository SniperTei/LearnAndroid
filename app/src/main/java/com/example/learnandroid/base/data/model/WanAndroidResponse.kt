package com.example.learnandroid.base.data.model

import com.example.common_module.network.BaseNetworkResponse

data class WanAndroidResponse<T>(val errorCode: Int, val errorMsg: String, val data: T): BaseNetworkResponse<T>() {
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