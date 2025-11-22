package com.sniper.webbox.network.config

/**
 * 网络配置管理类
 * 使用单例模式，允许动态修改配置项
 */
object NetworkConfig {
    // 基础URL
    // private var baseUrl: String = "http://192.168.130.128:8000"
    private var baseUrl: String = "http://10.0.2.2:8000"
    
    // 是否启用日志
    var logEnable: Boolean = true
    
    // 默认超时时间（毫秒）
    var defaultTimeout: Long = 30000
    
    // 连接超时时间（毫秒）
    var connectTimeout: Long = 15000
    
    // 读取超时时间（毫秒）
    var readTimeout: Long = 30000
    
    // 写入超时时间（毫秒）
    var writeTimeout: Long = 30000
    
    // 是否启用缓存
    var cacheEnable: Boolean = false
    
    // 缓存大小（字节）
    var cacheSize: Long = 10 * 1024 * 1024 // 默认10MB
    
    // 是否忽略SSL证书验证（仅用于测试环境）
    var ignoreSSLVerify: Boolean = false
    
    // 请求头中添加的默认User-Agent
    var userAgent: String = "WebBox-Android"
    
    // 获取基础URL
    fun getBaseUrl(): String {
        return baseUrl
    }
    
    // 设置基础URL（允许在运行时切换环境）
    fun setBaseUrl(url: String) {
        baseUrl = url.trimEnd('/')
    }
    
    /**
     * 环境类型枚举
     */
    enum class EnvironmentType {
        DEVELOP,    // 开发环境
        TEST,       // 测试环境
        PRE_PROD,   // 预发布环境
        PROD        // 生产环境
    }
    
    /**
     * 切换到指定环境
     * @param envType 环境类型
     * @param customUrl 自定义URL（如果指定了此参数，则使用自定义URL而不是预设的环境URL）
     */
    fun switchEnvironment(envType: EnvironmentType, customUrl: String? = null) {
        baseUrl = customUrl ?: when (envType) {
            EnvironmentType.DEVELOP -> "http://10.0.2.2:8000"
            EnvironmentType.TEST -> "https://test-api.example.com"
            EnvironmentType.PRE_PROD -> "https://pre-api.example.com"
            EnvironmentType.PROD -> "https://api.example.com"
        }
        
        // 根据环境自动设置日志和SSL验证
        when (envType) {
            EnvironmentType.DEVELOP, EnvironmentType.TEST -> {
                logEnable = true
                ignoreSSLVerify = true
            }
            EnvironmentType.PRE_PROD -> {
                logEnable = true
                ignoreSSLVerify = false
            }
            EnvironmentType.PROD -> {
                logEnable = false
                ignoreSSLVerify = false
            }
        }
    }
    
    /**
     * 重置所有配置到默认值
     */
    fun resetToDefault() {
        baseUrl = "https://api.example.com"
        logEnable = true
        defaultTimeout = 30000
        connectTimeout = 15000
        readTimeout = 30000
        writeTimeout = 30000
        cacheEnable = false
        cacheSize = 10 * 1024 * 1024
        ignoreSSLVerify = false
        userAgent = "WebBox-Android"
    }
}