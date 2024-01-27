package com.example.home_module.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.common_library.app.base.viewmodel.BaseViewModel
import com.example.common_library.ext.parseException
import com.example.common_library.ext.parseResult
import com.example.common_library.network.state.ListDataUiState
import com.example.common_library.network.state.ResultState
import com.example.home_module.data.bean.HomeBannerItemBean
import com.example.home_module.data.bean.HomeListItemBean
import com.example.home_module.data.repository.HomeRepository
import kotlinx.coroutines.launch

class RequestHomeVM: BaseViewModel() {
    private val TAG = "HomeViewModel"

    private var mPageNo = 0

    private val mBannerData = MutableLiveData<ResultState<ArrayList<HomeBannerItemBean>>>()

    private val mHomeList = MutableLiveData<ListDataUiState<HomeListItemBean>>()

    private val mHomeRepository = HomeRepository()
    // val listDataUiState = ListDataUiState()
    fun getHomeBanner() {
        viewModelScope.launch {
            runCatching {
                mBannerData.value = ResultState.onLoading("loading...")
                mHomeRepository.getBannerFromServer()
            }.onSuccess {
                Log.d("HomeViewModel", "getBannerFromServer success : $it")
                if (it.isResponseSuccess()) {
                    mBannerData.parseResult(it.getResponseData())
                }
            }.onFailure {
                Log.d("HomeViewModel", "getBannerFromServer failure")
                mBannerData.parseException(it)
            }
        }
    }

    fun getHomeList(isRefresh: Boolean) {
        if (isRefresh) {
            mPageNo = 0
        }
        viewModelScope.launch {
            runCatching {
                mHomeRepository.getHomeListFromServer(mPageNo)
            }.onSuccess {
                val isEmpty = it.data.total > 0
                val hasMore = it.data.size * (it.data.curPage + 1) < it.data.total
                val datas: ArrayList<HomeListItemBean> = it.data.datas
                mHomeList.value = ListDataUiState(true, isRefresh = true, isEmpty = isEmpty, hasMore = hasMore, listData = datas)
                Log.d("HomeViewModel", "getHomeListFromServer success")
            }.onFailure {
                mHomeList.value = ListDataUiState(false, "myerror", listData = error("errmsg"))
                Log.d("HomeViewModel", "getHomeListFromServer failure")
            }
        }
    }

    fun getBannerData(): LiveData<ResultState<ArrayList<HomeBannerItemBean>>> {
        return mBannerData
    }

    fun getHomeListData(): LiveData<ListDataUiState<HomeListItemBean>> {
        return mHomeList
    }
}