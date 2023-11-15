package com.example.learnandroid.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface WanAndroidService {
    // https://www.wanandroid.com/user/login
    @POST("user/login")
    @FormUrlEncoded
    public fun login(@Field("username") username: String, @Field("username") password: String): Call<ResponseBody>
}