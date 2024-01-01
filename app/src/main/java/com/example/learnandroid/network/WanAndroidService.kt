package com.example.learnandroid.network

import com.example.learnandroid.data.model.bean.HomeBannerItemBean
import com.example.learnandroid.data.model.bean.WanAndroidResponse
import retrofit2.http.GET


interface WanAndroidService {
    // https://www.wanandroid.com/user/login
//    @POST("user/login")
//    @FormUrlEncoded
//    public fun login(@Field("username") username: String, @Field("username") password: String): Call<ResponseBody>

    // WanAndroid 首页
    // https://www.wanandroid.com/article/list/0/json
//    @GET("article/list/{pageIndex}/json")
//    public fun getHomeListApi(@Path("pageIndex") index: Int): Call<WanResponseBean>

    // WanAndroid Banner
    // https://www.wanandroid.com/banner/json
    @GET("banner/json")
    suspend fun getBannerApi(): WanAndroidResponse<ArrayList<HomeBannerItemBean>>
    
}