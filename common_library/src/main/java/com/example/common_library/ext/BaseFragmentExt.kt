package com.example.common_library.ext

import com.example.common_library.network.AppException
import com.example.common_library.network.state.ResultState

fun <T> parseState(resultState: ResultState<T>,
                   onSuccess: (T) -> Unit,
                   onError: ((AppException) -> Unit)? = null,
                   onLoading: ((String) -> Unit)? = null) {
    when (resultState) {
        is ResultState.Loading -> {
            if (onLoading != null) {
                onLoading(resultState.loadingMsg)
            }
        }
        is ResultState.Success -> {
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            if (onError != null) {
                onError(resultState.error)
            }
        }
    }
}
