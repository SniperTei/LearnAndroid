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

        fun <T> onLoading(): ResultState<T> {
            return Loading
        }
    }

    data class Success<out T>(val data: T): ResultState<T>()
    data class Error(val exception: AppException): ResultState<Nothing>()
    object Loading: ResultState<Nothing>()
}

/**
 * 处理返回值
 * @param result 请求结果
 */
fun <T> MutableLiveData<ResultState<T>>.paresResult(result: BaseNetworkResponse<T>) {
    value = when {
        result.isResponseSuccess() -> {
            ResultState.onSuccess(result.getResponseData())
        }
        else -> {
            ResultState.onError(AppException(result.getResponseCode(), result.getResponseMsg()))
        }
    }
}

/**
 * 不处理返回值 直接返回请求结果
 * @param result 请求结果
 */
fun <T> MutableLiveData<ResultState<T>>.paresResult(result: T) {
    value = ResultState.onSuccess(result)
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ResultState<T>>.paresException(e: Throwable) {
    this.value = ResultState.onError(ExceptionHandle.handleException(e))
}