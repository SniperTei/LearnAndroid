package com.example.common_module.ext

import androidx.lifecycle.MutableLiveData
import com.example.common_module.network.AppException
import com.example.common_module.network.BaseNetworkResponse
import com.example.common_module.network.ExceptionHandle
import com.example.common_module.network.state.ResultState

/**
 * 处理返回值
 * @param result 请求结果
 */
fun <T> MutableLiveData<ResultState<T>>.parseResult(result: BaseNetworkResponse<T>) {
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
fun <T> MutableLiveData<ResultState<T>>.parseResult(result: T) {
    value = ResultState.onSuccess(result)
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ResultState<T>>.parseException(e: Throwable) {
    this.value = ResultState.onError(ExceptionHandle.handleException(e))
}
