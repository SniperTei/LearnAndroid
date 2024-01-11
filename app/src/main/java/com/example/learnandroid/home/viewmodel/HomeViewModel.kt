package com.example.learnandroid.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common_module.ext.parseException
import com.example.common_module.ext.parseResult
import com.example.common_module.network.state.ResultState
import com.example.learnandroid.base.data.model.WanAndroidResponse
import com.example.learnandroid.home.model.bean.HomeBannerItemBean
import com.example.learnandroid.home.model.bean.HomeListItemBean
import com.example.learnandroid.home.model.bean.PagerBean
import com.example.learnandroid.home.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val TAG = "HomeViewModel"

    private val bannerData = MutableLiveData<ResultState<ArrayList<HomeBannerItemBean>>>()

    private val homeList = MutableLiveData<WanAndroidResponse<PagerBean<ArrayList<HomeListItemBean>>>>()
    
    private val homeRepository = HomeRepository()

    fun getHomeBanner() {
        viewModelScope.launch {
            runCatching {
                bannerData.value = ResultState.onLoading("loading...")
                homeRepository.getBannerFromServer()
            }.onSuccess {
                Log.d("HomeViewModel", "getBannerFromServer success : $it")
                if (it.isResponseSuccess()) {
                    bannerData.parseResult(it.getResponseData())
                }
            }.onFailure {
                Log.d("HomeViewModel", "getBannerFromServer failure")
                bannerData.parseException(it)
            }
        }
    }

    fun getHomeList() {
        viewModelScope.launch {
            runCatching {
                val homeListResult = homeRepository.getHomeListFromServer()
                Log.d("HomeViewModel", "homeListResult: $homeListResult")
                 homeList.value = homeListResult
            }.onSuccess {
                Log.d("HomeViewModel", "getHomeListFromServer success")
            }.onFailure {
                Log.d("HomeViewModel", "getHomeListFromServer failure")
            }
        }
    }

    fun getBannerData(): LiveData<ResultState<ArrayList<HomeBannerItemBean>>> {
        return bannerData
    }

    fun getHomeListData(): LiveData<WanAndroidResponse<PagerBean<ArrayList<HomeListItemBean>>>> {
        return homeList
    }
}