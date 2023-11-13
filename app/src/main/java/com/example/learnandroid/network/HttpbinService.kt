package com.example.learnandroid.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HttpbinService {
    // retrofit get
    @GET("get")
    public fun get(@Query("name") name: String, @Query("age") age: Int): Call<ResponseBody>

    // retrofit post
    @POST("post")
    @FormUrlEncoded
    public fun post(@Field("username") username: String, @Field("username") password: String): Call<ResponseBody>
}