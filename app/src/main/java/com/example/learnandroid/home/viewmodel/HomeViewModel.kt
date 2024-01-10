package com.example.learnandroid.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnandroid.data.model.bean.WanAndroidResponse
import com.example.learnandroid.home.model.bean.HomeBannerItemBean
import com.example.learnandroid.home.repository.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val titleName = MutableLiveData<String>()

    private val banner = MutableLiveData<WanAndroidResponse<ArrayList<HomeBannerItemBean>>>()
    
    private val homeRepository = HomeRepository()

    fun changeTitleName(name: String) {
        titleName.value = name
    }

    fun getTitleName(): LiveData<String> {
        return titleName
    }

    fun getHomeBanner() {
        viewModelScope.launch {
            // val response = homeRepository.getBannerFromServer()
            // Log.d("HomeViewModel", "response: ${response}")
            runCatching {
                val yucky = homeRepository.getBannerFromServer()
                // 设置banner
                banner.value = yucky
            }.onSuccess {
                Log.d("HomeViewModel", "getBannerFromServer success")
            }.onFailure {
                Log.d("HomeViewModel", "getBannerFromServer failure")
            }
        }
    }

    fun getBanner(): LiveData<WanAndroidResponse<ArrayList<HomeBannerItemBean>>> {
        return banner
    }
}