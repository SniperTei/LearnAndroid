package com.example.learnandroid.network

import com.example.common_module.network.state.ResultState
import com.example.learnandroid.data.model.bean.WanAndroidResponse
import com.example.learnandroid.home.model.bean.HomeBannerItemBean
import com.example.learnandroid.home.model.bean.HomeDataBean
import com.example.learnandroid.home.model.bean.HomeListItemBean
import retrofit2.http.GET
import retrofit2.http.Path


interface WanAndroidService {

    companion object { // 环境配置
        const val BASE_URL = "https://www.wanandroid.com/"
    }

    // https://www.wanandroid.com/user/login
//    @POST("user/login")
//    @FormUrlEncoded
//    public fun login(@Field("username") username: String, @Field("username") password: String): Call<ResponseBody>

    // WanAndroid 首页
//    https://www.wanandroid.com/article/list/0/json
    @GET("article/list/{pageIndex}/json")
    suspend fun getHomeListApi(@Path("pageIndex") index: Int): WanAndroidResponse<HomeDataBean<ArrayList<HomeListItemBean>>>

    // WanAndroid Banner
    // https://www.wanandroid.com/banner/json
    @GET("banner/json")
    suspend fun getBannerApi(): WanAndroidResponse<ArrayList<HomeBannerItemBean>>
    
}