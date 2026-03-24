package com.sniper.webbox.container

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * 容器配置数据类
 *
 * 通过配置文件驱动容器行为，实现"一次编译，多应用运行"
 *
 * 使用方式：
 * 1. 在 assets 目录放置 container_config.json
 * 2. 容器启动时自动加载配置
 * 3. 根据配置初始化 WebView 和功能
 */
data class ContainerConfig(
    /**
     * 应用唯一标识
     * 用于隔离存储、Cookie 等
     */
    @SerializedName("appId")
    val appId: String,

    /**
     * 应用名称
     * 显示在标题栏、通知栏等位置
     */
    @SerializedName("appName")
    val appName: String,

    /**
     * 首页 URL
     * 容器启动后加载的第一个页面
     */
    @SerializedName("homeUrl")
    val homeUrl: String,

    /**
     * 允许访问的域名白名单
     * 用于安全控制，防止访问恶意网站
     */
    @SerializedName("allowedDomains")
    val allowedDomains: List<String> = emptyList(),

    /**
     * 启用的功能列表
     * 控制哪些 JS Bridge Handler 可用
     *
     * 可选值：
     * - device: 设备信息
     * - camera: 拍照/相册
     * - location: 地理位置
     * - share: 分享
     * - clipboard: 剪贴板
     * - network: 网络状态
     * - vibrator: 震动
     * - scan: 扫码
     * - file: 文件操作
     * - picker: 选择器
     * - notification: 通知
     * - webview: WebView 控制
     */
    @SerializedName("enabledFeatures")
    val enabledFeatures: List<String> = emptyList(),

    /**
     * 主题配置
     */
    @SerializedName("theme")
    val theme: ThemeConfig = ThemeConfig(),

    /**
     * 网络安全配置
     */
    @SerializedName("networkSecurity")
    val networkSecurity: NetworkSecurityConfig = NetworkSecurityConfig(),

    /**
     * WebView 行为配置
     */
    @SerializedName("webview")
    val webview: WebViewConfig = WebViewConfig()
) {

    /**
     * 主题配置
     */
    data class ThemeConfig(
        /**
         * 状态栏颜色（十六进制，如 #FF6200EE）
         */
        @SerializedName("statusBarColor")
        val statusBarColor: String = "#FF000000",

        /**
         * 工具栏背景颜色
         */
        @SerializedName("toolbarColor")
        val toolbarColor: String = "#FFFFFFFF",

        /**
         * 工具栏标题颜色
         */
        @SerializedName("toolbarTitleColor")
        val toolbarTitleColor: String = "#FF000000",

        /**
         * 是否显示导航栏
         */
        @SerializedName("showToolbar")
        val showToolbar: Boolean = true,

        /**
         * 是否显示返回按钮
         */
        @SerializedName("showBackButton")
        val showBackButton: Boolean = true,

        /**
         * 是否显示关闭按钮
         */
        @SerializedName("showCloseButton")
        val showCloseButton: Boolean = false
    )

    /**
     * 网络安全配置
     */
    data class NetworkSecurityConfig(
        /**
         * 是否允许明文流量（HTTP）
         * 生产环境建议设为 false
         */
        @SerializedName("allowCleartext")
        val allowCleartext: Boolean = false,

        /**
         * 额外的信任域名（用于自签名证书）
         */
        @SerializedName("trustedDomains")
        val trustedDomains: List<String> = emptyList()
    )

    /**
     * WebView 行为配置
     */
    data class WebViewConfig(
        /**
         * 是否启用 JavaScript
         */
        @SerializedName("javaScriptEnabled")
        val javaScriptEnabled: Boolean = true,

        /**
         * 是否启用 DOM 存储
         */
        @SerializedName("domStorageEnabled")
        val domStorageEnabled: Boolean = true,

        /**
         * 是否启用数据库
         */
        @SerializedName("databaseEnabled")
        val databaseEnabled: Boolean = true,

        /**
         * 是否启用缓存
         */
        @SerializedName("cacheEnabled")
        val cacheEnabled: Boolean = true,

        /**
         * User-Agent 后缀
         * 会追加到默认 User-Agent 后面
         */
        @SerializedName("userAgentSuffix")
        val userAgentSuffix: String? = null,

        /**
         * 默认缩放比例（1.0 = 100%）
         */
        @SerializedName("defaultZoom")
        val defaultZoom: Float = 1.0f,

        /**
         * 最小缩放比例
         */
        @SerializedName("minZoom")
        val minZoom: Float = 0.5f,

        /**
         * 最大缩放比例
         */
        @SerializedName("maxZoom")
        val maxZoom: Float = 2.0f,

        /**
         * 页面加载超时时间（毫秒）
         */
        @SerializedName("pageLoadTimeout")
        val pageLoadTimeout: Long = 30000
    )

    /**
     * 检查是否启用了指定功能
     */
    fun isFeatureEnabled(feature: String): Boolean {
        return enabledFeatures.contains(feature)
    }

    /**
     * 检查域名是否在白名单中
     */
    fun isDomainAllowed(domain: String): Boolean {
        return allowedDomains.any { allowedDomain ->
            domain.endsWith(allowedDomain) || allowedDomain.endsWith(domain)
        }
    }

    /**
     * 转换为 JSON 字符串
     */
    fun toJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        /**
         * 从 JSON 字符串解析
         */
        fun fromJson(json: String?): ContainerConfig? {
            return try {
                if (json.isNullOrBlank()) null
                else Gson().fromJson(json, ContainerConfig::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }
}

/**
 * 所有可用的功能列表
 */
object AvailableFeatures {
    const val DEVICE = "device"
    const val CAMERA = "camera"
    const val LOCATION = "location"
    const val SHARE = "share"
    const val CLIPBOARD = "clipboard"
    const val NETWORK = "network"
    const val VIBRATOR = "vibrator"
    const val SCAN = "scan"
    const val FILE = "file"
    const val PICKER = "picker"
    const val NOTIFICATION = "notification"
    const val WEBVIEW = "webview"

    /**
     * 获取所有可用功能列表
     */
    fun allFeatures(): List<String> {
        return listOf(
            DEVICE, CAMERA, LOCATION, SHARE, CLIPBOARD,
            NETWORK, VIBRATOR, SCAN, FILE, PICKER,
            NOTIFICATION, WEBVIEW
        )
    }
}
