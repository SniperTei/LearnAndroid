package com.example.learnandroid.home.repository

import com.example.learnandroid.data.model.bean.WanAndroidResponse
import com.example.learnandroid.home.model.bean.HomeBannerItemBean
import com.example.learnandroid.home.model.bean.HomeDataBean
import com.example.learnandroid.home.model.bean.HomeListItemBean
import com.example.learnandroid.network.wanAndroidService

class HomeRepository {

    // instance
    companion object {
         val INSTANCE: HomeRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
             HomeRepository()
         }
    }

    // 首页轮播图
    suspend fun getBannerFromServer(): WanAndroidResponse<ArrayList<HomeBannerItemBean>> {
        return wanAndroidService.getBannerApi()
    }

    fun getBannerFromDatabase() {

    }
    // mock
    fun getBannerFromMock() {

    }

    // 首页列表
    suspend fun getHomeListFromServer(): WanAndroidResponse<HomeDataBean<ArrayList<HomeListItemBean>>> {
        return wanAndroidService.getHomeListApi(0)
    }

    fun getHomeListFromDatabase() {

    }

    // mock
    fun getHomeListFromMock() {

    }
}