package com.example.common_module.network.state

import androidx.lifecycle.MutableLiveData
import com.example.common_module.network.AppException
import com.example.common_module.network.BaseNetworkResponse
import com.example.common_module.network.ExceptionHandle

sealed class ResultState<out T> {

    companion object {
        fun <T> onSuccess(data: T): ResultState<T> {
            return Success(data)
        }

        fun <T> onError(exception: AppException): ResultState<T> {
            return Error(exception)
        }

        fun <T> onLoading(loadingMsg: String): ResultState<T> {
            return Loading(loadingMsg)
        }
    }

    data class Success<out T>(val data: T): ResultState<T>()
    data class Error(val error: AppException): ResultState<Nothing>()
    data class Loading(val loadingMsg:String): ResultState<Nothing>()
}