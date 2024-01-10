package com.example.learnandroid.network

import com.example.common_module.network.BaseNetworkAPI
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val wanAndroidService: WanAndroidService by lazy {
    NetworkAPI.INSTANCE.getAPI(WanAndroidService::class.java, WanAndroidService.BASE_URL)
}

class NetworkAPI: BaseNetworkAPI() {

    // NetworkAPI单例
    companion object {
        val INSTANCE: NetworkAPI by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkAPI()
        }
    }

    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        return builder
    }

    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        builder.apply { 
            addConverterFactory(GsonConverterFactory.create())
         }
        return builder
    }
}