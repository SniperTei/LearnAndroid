package com.example.common_module.network

abstract class BaseNetworkResponse<T> {
    
    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String

    abstract fun getResponseData(): T

    abstract fun isResponseSuccess(): Boolean
}