package com.example.learnandroid.viewmodel

import android.util.Log
import com.example.learnandroid.network.WanAndroidService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class HomelistViewModel(private val wanandroidService: WanAndroidService) {

    // 首页列表
    fun getHomeList(index: Int) {
        val call = wanandroidService?.getHomeList(index)
        call?.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("HomeListViewModel", "onFailure: ${t.message}")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Log.d("HomeListViewModel", "onResponse: ${response.body()?.string()}")
            }
        })
    }

}