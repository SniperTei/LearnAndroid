package com.example.home_module.data.repository

import com.example.common_library.app.base.bean.AppResponse
import com.example.home_module.data.bean.HomeBannerItemBean
import com.example.home_module.data.bean.HomeListItemBean
import com.example.home_module.data.bean.PagerBean
import com.example.home_module.data.repository.server.homeService

class HomeRepository {
    // instance
    companion object {
        val INSTANCE: HomeRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            HomeRepository()
        }
    }

    // 首页轮播图
    suspend fun getBannerFromServer(): AppResponse<ArrayList<HomeBannerItemBean>> {
        return homeService.getBannerApi()
    }

    fun getBannerFromDatabase() {

    }
    // mock
    fun getBannerFromMock() {

    }

    // 首页列表
    suspend fun getHomeListFromServer(pageNo: Int): AppResponse<PagerBean<ArrayList<HomeListItemBean>>> {
        return homeService.getHomeListApi(pageNo)
    }

    fun getHomeListFromDatabase() {

    }

    // mock
    fun getHomeListFromMock() {

    }
}