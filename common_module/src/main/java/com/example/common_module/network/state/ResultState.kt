package com.example.common_module.network.state

import androidx.lifecycle.MutableLiveData

sealed class ResultState<out T> {

    companion object {
        fun <T> onSuccess(data: T): ResultState<T> {
            return Success(data)
        }

        fun <T> onError(exception: Throwable): ResultState<T> {
            return Error(exception)
        }

        fun <T> onLoading(): ResultState<T> {
            return Loading
        }
    }

    data class Success<out T>(val data: T): ResultState<T>()
    data class Error(val exception: Throwable): ResultState<Nothing>()
    object Loading: ResultState<Nothing>()
}

fun MutableLiveData<ResultState<Nothing>>.postLoading() {
    this.postValue(ResultState.onLoading())
}

fun <T> MutableLiveData<ResultState<T>>.postSuccess(data: T) {
    this.postValue(ResultState.onSuccess(data))
}

fun <T> MutableLiveData<ResultState<T>>.postError(exception: Throwable) {
    this.postValue(ResultState.onError(exception))
}