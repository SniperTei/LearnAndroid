package com.sniper.webbox.config

/**
 * 应用配置常量
 */
object AppConfig {
    /**
     * H5页面URL配置
     */
    object H5Url {
        // 本地开发环境（使用 adb reverse 端口转发后可用 localhost）
        const val LOCAL_DEV = "http://localhost:5173"
        // 测试环境（云服务器）
        const val TEST = "http://app-test.sniper14.online"

        // 生产环境
        const val PROD = "https://app.sniper14.online"

        // 当前使用的环境（切换环境修改这里）
        const val CURRENT = LOCAL_DEV
    }

    /**
     * API配置
     */
    object ApiUrl {
        // 本地开发环境（模拟器访问主机后端）
        const val LOCAL_DEV = "http://10.0.2.2:8000"
        // 测试环境（云服务器）
        const val TEST = "http://api-test.sniper14.online"

        // 生产环境
        const val PROD = "https://api.sniper14.online"
    }

    /**
     * 环境类型
     */
    enum class Environment {
        LOCAL_DEV,   // 本地开发
        TEST,        // 测试环境
        PROD         // 生产环境
    }

    /**
     * 获取当前环境
     */
    fun getCurrentEnvironment(): Environment {
        return when (H5Url.CURRENT) {
            H5Url.LOCAL_DEV -> Environment.LOCAL_DEV
            H5Url.TEST -> Environment.TEST
            H5Url.PROD -> Environment.PROD
            else -> Environment.LOCAL_DEV
        }
    }

    /**
     * 是否为开发环境
     */
    fun isDevelopment(): Boolean {
        return getCurrentEnvironment() == Environment.LOCAL_DEV
    }
}
