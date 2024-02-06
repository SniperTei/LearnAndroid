package com.example.home_module.data.repository.server

import com.example.core_library.data.bean.AppResponse
import com.example.common_library.app.network.AppEnvironment
import com.example.core_library.network.AppNetworkAPI
import com.example.home_module.data.bean.HomeBannerItemBean
import com.example.home_module.data.bean.HomeListItemBean
import com.example.home_module.data.bean.PagerBean
import retrofit2.http.GET
import retrofit2.http.Path

val homeService: HomeService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    AppNetworkAPI.INSTANCE.getAPI(HomeService::class.java, AppEnvironment.getBaseURL())
}

interface HomeService {
    @GET("article/list/{pageIndex}/json")
    suspend fun getHomeListApi(@Path("pageIndex") index: Int): AppResponse<PagerBean<ArrayList<HomeListItemBean>>>

    // WanAndroid Banner
    // https://www.wanandroid.com/banner/json
    @GET("banner/json")
    suspend fun getBannerApi(): AppResponse<ArrayList<HomeBannerItemBean>>
}