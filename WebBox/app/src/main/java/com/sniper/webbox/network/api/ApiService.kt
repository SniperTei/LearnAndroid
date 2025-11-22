package com.sniper.webbox.network.api

import com.sniper.webbox.network.manager.NetworkManager
import com.sniper.webbox.network.exception.NetworkException
import com.sniper.webbox.network.api.ApiResponse
import com.sniper.webbox.network.enums.ParamType
import com.sniper.webbox.network.enums.RequestMethod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * API服务基类
 * 用于执行网络请求并处理通用逻辑
 */
open class ApiService {

    /**
     * 执行网络请求
     * @param request ApiRequest接口实现
     * @return 响应字符串
     */
    protected suspend fun execute(request: ApiRequest): String {
        return withContext(Dispatchers.IO) {
            try {
                NetworkManager.execute(request)
            } catch (e: Exception) {
                throw handleException(e)
            }
        }
    }

    /**
     * 执行网络请求并解析为ResponseResult
     * @param request ApiRequest接口实现
     * @return ApiResponse对象
     */
    protected suspend fun <T> executeAndParse(request: ApiRequest, clazz: Class<T>): ApiResponse<T> {
        val responseString = execute(request)
        return parseResponse(responseString, clazz)
    }

    /**
     * 解析响应结果
     * @param responseString 响应字符串
     * @param clazz 数据类型
     * @return ApiResponse对象
     */
    private fun <T> parseResponse(responseString: String, clazz: Class<T>): ApiResponse<T> {
        try {
            val adapter = com.google.gson.Gson().getAdapter(ApiResponse::class.java)
            val result = adapter.fromJson(responseString) as ApiResponse<T>
            
            if (!result.isSuccess()) {
                throw NetworkException(result.msg, result.statusCode)
            }
            
            return result
        } catch (e: Exception) {
            if (e is NetworkException) {
                throw e
            }
            throw com.sniper.webbox.network.exception.ParseException(e.message ?: "数据解析失败")
        }
    }

    /**
     * 处理异常
     * @param e 异常
     * @return 封装后的异常
     */
    private fun handleException(e: Exception): Exception {
        return when (e) {
            is java.io.IOException -> {
                com.sniper.webbox.network.exception.ConnectException(e.message ?: "网络连接失败")
            }
            is com.google.gson.JsonParseException,
            is java.lang.IllegalStateException -> {
                com.sniper.webbox.network.exception.ParseException(e.message ?: "数据解析失败")
            }
            else -> {
                e
            }
        }
    }
}