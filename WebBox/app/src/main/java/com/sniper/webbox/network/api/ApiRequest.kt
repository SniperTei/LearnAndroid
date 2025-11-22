package com.sniper.webbox.network.api

import com.sniper.webbox.network.config.NetworkConfig
import com.sniper.webbox.network.enums.ParamType
import com.sniper.webbox.network.enums.RequestMethod

interface ApiRequest {
    /**
     * 接口路径（不含基础域名，如 "/user/login"）
     */
    fun getApiPath(): String

    /**
     * 请求方法（GET/POST 等）
     */
    fun getRequestMethod(): RequestMethod

    /**
     * 参数类型（配合 getParams 方法使用）
     */
    fun getParamType(): ParamType
    
    /**
     * 请求参数（根据 ParamType 返回对应类型的对象）
     * 示例：ParamType.MAP 返回 Map<String, Any>，ParamType.JSON_STRING 返回 String
     */
    fun getParams(): Any?
    
    /**
     * 请求超时时间（毫秒）
     * 默认返回 30000 毫秒（30秒）
     */
    fun getTimeoutMillis(): Long = NetworkConfig.defaultTimeout
    
    /**
     * 是否需要添加认证头
     * 默认返回 true，表示需要认证
     */
    fun needAuth(): Boolean = true
    
    /**
     * 获取请求头
     * 返回自定义请求头的 Map
     */
    fun getHeaders(): Map<String, String> = emptyMap()
    
    /**
     * 是否启用缓存
     * 默认返回 false，表示不启用缓存
     */
    fun enableCache(): Boolean = NetworkConfig.cacheEnable
    
    /**
     * 缓存有效期（毫秒）
     * 默认返回 0，表示不缓存
     */
    fun getCacheTimeMillis(): Long = 0
    
    /**
     * 响应数据类型
     * 返回响应数据的 Class 对象，用于解析 JSON 等数据
     * 如果返回 null，则不进行自动解析
     */
    fun getResponseType(): Class<*>? = null
}