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

class HomelistViewModel(private val wanandroidService: WanAndroidService) {

    private val TAG = "HomeListViewModel"

    // 首页列表
    fun getHomeListApi(index: Int) {
        wanandroidService.getHomeListApi(index).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.rxjava3.core.Observer<WanResponseBean> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, "onSubscribe: $d")
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "onError: $e")
                }

                override fun onNext(t: WanResponseBean) {
                    Log.d(TAG, "onNext: $t")
                }

                override fun onComplete() {
                    Log.d(TAG, "onComplete: ")
                }
            })


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