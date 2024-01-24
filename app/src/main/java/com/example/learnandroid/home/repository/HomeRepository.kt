package com.example.learnandroid.home.repository


import com.example.learnandroid.app.data.bean.AppResponse
import com.example.learnandroid.home.model.bean.HomeBannerItemBean
import com.example.learnandroid.home.model.bean.HomeListItemBean
import com.example.learnandroid.home.model.bean.PagerBean
import com.example.learnandroid.app.data.repository.server.wanAndroidService

class HomeRepository {

    // instance
    companion object {
         val INSTANCE: HomeRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
             HomeRepository()
         }
    }

    // 首页轮播图
    suspend fun getBannerFromServer(): AppResponse<ArrayList<HomeBannerItemBean>> {
        return wanAndroidService.getBannerApi()
    }

    fun getBannerFromDatabase() {

    }
    // mock
    fun getBannerFromMock() {

    }

    // 首页列表
    suspend fun getHomeListFromServer(pageNo: Int): AppResponse<PagerBean<ArrayList<HomeListItemBean>>> {
        return wanAndroidService.getHomeListApi(pageNo)
    }

    fun getHomeListFromDatabase() {

    }

    // mock
    fun getHomeListFromMock() {

    }
}