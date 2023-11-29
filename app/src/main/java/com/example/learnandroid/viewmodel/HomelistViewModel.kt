package com.example.learnandroid.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import com.example.learnandroid.bean.WanResponseBean
import com.example.learnandroid.network.WanAndroidService
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class HomelistViewModel() {

    private val TAG = "HomeListViewModel"

    private lateinit var mWanAndroidService: WanAndroidService

    // 构造函数
    init {
        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()

        mWanAndroidService = retrofit.create(WanAndroidService::class.java)
    }

    // 首页列表
    fun getHomeListApi(index: Int): Observable<WanResponseBean> {
        return mWanAndroidService.getHomeListApi(index)


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
    }
}