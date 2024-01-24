package com.example.learnandroid.app.data.repository.server

import com.example.learnandroid.app.data.bean.AppResponse
import com.example.learnandroid.home.model.bean.HomeBannerItemBean
import com.example.learnandroid.home.model.bean.HomeListItemBean
import com.example.learnandroid.home.model.bean.PagerBean
import retrofit2.http.GET
import retrofit2.http.Path

val wanAndroidService: AppService by lazy {
    AppNetworkAPI.INSTANCE.getAPI(AppService::class.java, AppEnvironment.getBaseURL())
}

interface AppService {

    // https://www.wanandroid.com/user/login
//    @POST("user/login")
//    @FormUrlEncoded
//    public fun login(@Field("username") username: String, @Field("username") password: String): Call<ResponseBody>

    // WanAndroid 首页
//    https://www.wanandroid.com/article/list/0/json
    @GET("article/list/{pageIndex}/json")
    suspend fun getHomeListApi(@Path("pageIndex") index: Int): AppResponse<PagerBean<ArrayList<HomeListItemBean>>>

    // WanAndroid Banner
    // https://www.wanandroid.com/banner/json
    @GET("banner/json")
    suspend fun getBannerApi(): AppResponse<ArrayList<HomeBannerItemBean>>
    
}