package com.sniper.webbox.network.exception

/**
 * 网络异常类
 * 用于统一处理网络请求中的各种异常
 */
open class NetworkException(override val message: String, val errorCode: Int = 0) : Exception(message)

/**
 * 服务器异常
 */
class ServerException(message: String, errorCode: Int = 500) : NetworkException(message, errorCode)

/**
 * 网络连接异常
 */
class ConnectException(message: String = "网络连接失败") : NetworkException(message, -1)

/**
 * 数据解析异常
 */
class ParseException(message: String = "数据解析失败") : NetworkException(message, -2)

/**
 * 认证失败异常
 */
class AuthException(message: String = "认证失败") : NetworkException(message, 401)