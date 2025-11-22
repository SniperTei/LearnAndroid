package com.sniper.webbox.network.api

/**
 * API响应结果封装类
 * 用于统一处理服务器返回的数据格式
 * 遵循接口文档规范：code为字符串格式，成功为"000000"
 */
open class ApiResponse<T>(
    var code: String = "",          // 响应代码：成功="000000"，错误="A00xxx"
    var statusCode: Int = 200,       // HTTP状态码
    var msg: String = "",           // 响应消息
    var data: T? = null,            // 响应数据，错误时为null
    var timestamp: String = ""      // 服务器时间戳
) {
    /**
     * 是否请求成功
     */
    fun isSuccess(): Boolean {
        return code == "000000"
    }
}

/**
 * 空数据响应结果
 */
typealias EmptyResponse = ApiResponse<Unit>