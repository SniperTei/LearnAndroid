package com.example.learnandroid.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.learnandroid.bean.HomeBannerItemBean
import com.example.learnandroid.bean.WanResponseBean
import com.example.learnandroid.network.WanAndroidService
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.launch
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HomeViewModel: BaseViewModel() {
    private val TAG = "HomeListViewModel"

    // homeListLiveData
//    private var mHomeListData: Observable<WanResponseBean>? = null
    // homeBannerLiveData
    var mHomeBannerLiveData: MutableLiveData<WanResponseBean<ArrayList<HomeBannerItemBean>>> = MutableLiveData()

    private var mWanAndroidService: WanAndroidService

    // handler
    private var mHandler = android.os.Handler()

    // 构造函数
    init {
        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mWanAndroidService = retrofit.create(WanAndroidService::class.java)
    }

    // 首页列表
//    fun getHomeListApi(index: Int): Observable<WanResponseBean> {
//        return mWanAndroidService.getHomeListApi(index)


//        call?.enqueue(object : retrofit2.Callback<WanResponseBean> {
//            override fun onResponse(
//                call: Call<WanResponseBean>,
//                response: Response<WanResponseBean>
//            ) {
//                Log.d(TAG, "onResponse: ${response.body()}")
//                if (response.body() == null) {
//                    return
//                }
////                mHomeListData = Observable.just(response.body()!!)
//            }
//
//            override fun onFailure(call: Call<WanResponseBean>, t: Throwable) {
//                Log.d(TAG, "onFailure: ${t.message}")
//            }
//        })
//    }

    fun getBannerApi() {
        Log.d(TAG, "get banner api response before : ${mHomeBannerLiveData.value}")
        // scope.launch
        viewModelScope.launch {
            mHomeBannerLiveData.value = mWanAndroidService.getBannerApi()
            Log.d(TAG, "get banner api response after : ${mHomeBannerLiveData.value}")
        }
    }

//    public fun getHomeBannerLiveData(): MutableLiveData<WanResponseBean<ArrayList<HomeBannerItemBean>>> {
//        return mHomeBannerLiveData
//    }

//    fun getBannerApi(): Observable<WanResponseBean> {
//        return mWanAndroidService.getBannerApi()
//    }
}