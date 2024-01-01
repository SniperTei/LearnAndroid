package com.example.learnandroid.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.example.learnandroid.data.model.bean.HomeBannerItemBean
import com.example.learnandroid.data.model.bean.WanAndroidResponse

class HomeViewModel {
    // 轮播图数据
    var mBannerData: MutableLiveData<WanAndroidResponse<ArrayList<HomeBannerItemBean>>> = MutableLiveData()

    fun fetchBannerData() {

    }
}