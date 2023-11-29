package com.example.learnandroid.network

import io.reactivex.rxjava3.core.Observable
import com.example.learnandroid.bean.WanResponseBean
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path;


interface WanAndroidService {
    // https://www.wanandroid.com/user/login
//    @POST("user/login")
//    @FormUrlEncoded
//    public fun login(@Field("username") username: String, @Field("username") password: String): Call<ResponseBody>

    // WanAndroid 首页
    // https://www.wanandroid.com/article/list/0/json
    @GET("article/list/{pageIndex}/json")
    public fun getHomeListApi(@Path("pageIndex") index: Int): Observable<WanResponseBean>
    
}