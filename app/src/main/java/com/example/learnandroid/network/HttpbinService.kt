package com.example.learnandroid.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HttpbinService {

    /* retrofit get @GET是以get方式， 括号里的"get"是接口名 实际是
    * mRetrofit = Retrofit.Builder()
            .baseUrl("https://www.httpbin.org/")
            .build() 的  https://www.httpbin.org/get
    *  */
    @GET("get")
    public fun get(@Query("name") name: String, @Query("age") age: Int): Call<ResponseBody>

    // retrofit post
    @POST("post")
    @FormUrlEncoded
    public fun post(@Field("username") username: String, @Field("username") password: String): Call<ResponseBody>
}