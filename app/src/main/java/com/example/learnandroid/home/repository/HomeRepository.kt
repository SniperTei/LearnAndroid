package com.example.learnandroid.home.repository

import com.example.learnandroid.data.model.bean.WanAndroidResponse
import com.example.learnandroid.home.model.bean.HomeBannerItemBean
import com.example.learnandroid.network.wanAndroidService

class HomeRepository {

    // instance
    companion object {
         val INSTANCE: HomeRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
             HomeRepository()
         }
    }

    // 从服务器获取数据
    suspend fun getBannerFromServer(): WanAndroidResponse<ArrayList<HomeBannerItemBean>> {
        return wanAndroidService.getBannerApi()
    }

    fun getBannerFromDatabase() {

    }
    // mock数据
    fun getBannerFromMock() {

    }
}