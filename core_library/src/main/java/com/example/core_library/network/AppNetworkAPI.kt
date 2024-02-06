package com.example.core_library.network

import com.example.common_library.network.BaseNetworkAPI
import com.example.common_library.network.interceptor.log.LogInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppNetworkAPI: BaseNetworkAPI() {
    // NetworkAPI单例
    companion object {
        val INSTANCE: AppNetworkAPI by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppNetworkAPI()
        }
    }

    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            // 日志拦截器
            addInterceptor(LogInterceptor())
            // 超时时间 连接、读、写
            connectTimeout(40, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
        }
        return builder
    }

    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        builder.apply {
            addConverterFactory(GsonConverterFactory.create())
        }
        return builder
    }
}