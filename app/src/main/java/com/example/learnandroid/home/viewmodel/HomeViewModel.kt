package com.example.learnandroid.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnandroid.data.model.bean.WanAndroidResponse
import com.example.learnandroid.home.model.bean.HomeBannerItemBean
import com.example.learnandroid.home.model.bean.HomeDataBean
import com.example.learnandroid.home.model.bean.HomeListItemBean
import com.example.learnandroid.home.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val TAG = "HomeViewModel"

    private val banner = MutableLiveData<WanAndroidResponse<ArrayList<HomeBannerItemBean>>>()

    private val homeList = MutableLiveData<WanAndroidResponse<HomeDataBean<ArrayList<HomeListItemBean>>>>()
    
    private val homeRepository = HomeRepository()

    fun getHomeBanner() {
        viewModelScope.launch {
            runCatching {
                val bannerResult = homeRepository.getBannerFromServer()
                // 设置banner
                banner.value = bannerResult
            }.onSuccess {
                Log.d("HomeViewModel", "getBannerFromServer success")
            }.onFailure {
                Log.d("HomeViewModel", "getBannerFromServer failure")
            }
        }
    }

    fun getHomeList() {
        viewModelScope.launch {
            runCatching {
                val homeListResult = homeRepository.getHomeListFromServer()
                Log.d("HomeViewModel", "homeListResult: ${homeListResult}")
                 homeList.value = homeListResult
            }.onSuccess {
                Log.d("HomeViewModel", "getHomeListFromServer success")
            }.onFailure {
                Log.d("HomeViewModel", "getHomeListFromServer failure")
            }
        }
    }

    fun getBanner(): LiveData<WanAndroidResponse<ArrayList<HomeBannerItemBean>>> {
        return banner
    }

    fun getHomeListData(): LiveData<WanAndroidResponse<HomeDataBean<ArrayList<HomeListItemBean>>>> {
        return homeList
    }
}