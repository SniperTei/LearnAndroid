package com.example.common_module.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit

abstract class BaseNetworkApi {
    
    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            return setHttpClientBuilder(builder).build()
        }

    fun <T> getAPI(serviceClass: Class<T>, baseUrl: String): T {
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
        return setRetrofitBuilder(retrofitBuilder).build().create(serviceClass)
    }

    abstract fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder

    abstract fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder
}