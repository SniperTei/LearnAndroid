package com.example.common_library.network

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

object ExceptionHandle {
    fun handleException(e: Throwable?): AppException {
        val ex: AppException
        e?.let {
            when (it) {
                is HttpException -> {
                    ex = AppException(AppError.NETWORK_ERROR,e)
                    return ex
                }
                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                    ex = AppException(AppError.PARSE_ERROR,e)
                    return ex
                }
                is ConnectException -> {
                    ex = AppException(AppError.NETWORK_ERROR,e)
                    return ex
                }
                is javax.net.ssl.SSLException -> {
                    ex = AppException(AppError.SSL_ERROR,e)
                    return ex
                }
                is ConnectTimeoutException -> {
                    ex = AppException(AppError.TIMEOUT_ERROR,e)
                    return ex
                }
                is java.net.SocketTimeoutException -> {
                    ex = AppException(AppError.TIMEOUT_ERROR,e)
                    return ex
                }
                is java.net.UnknownHostException -> {
                    ex = AppException(AppError.TIMEOUT_ERROR,e)
                    return ex
                }
                is AppException -> return it

                else -> {
                    ex = AppException(AppError.UNKNOWN,e)
                    return ex
                }
            }
        }
        ex = AppException(AppError.UNKNOWN,e)
        return ex
    }
}