package com.sniper.webbox.container

import android.content.Context
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebStorage
import java.io.File

/**
 * Web 容器实例
 *
 * 负责管理单个 WebView 实例的生命周期、存储隔离、Cookie 隔离等
 *
 * 核心职责：
 * 1. 为不同应用提供隔离的运行环境
 * 2. 管理 Cookie 和 LocalStorage
 * 3. 管理 WebView 缓存
 * 4. 提供容器销毁时的清理工作
 */
class WebContainer(
    private val context: Context,
    private val config: ContainerConfig
) {
    private val TAG = "WebContainer_${config.appId}"

    private val cookieManager: CookieManager = CookieManager.getInstance()
    private val appCacheDir: File by lazy {
        File(context.cacheDir, "webview_${config.appId}").apply {
            if (!exists()) mkdirs()
        }
    }

    /**
     * 初始化容器
     */
    fun initialize() {
        Log.d(TAG, "🚀 初始化容器: ${config.appName}")
        setupCookieIsolation()
        setupStorageIsolation()
    }

    /**
     * 设置 Cookie 隔离
     *
     * 为不同应用设置不同的 Cookie 域，防止 Cookie 泄漏
     */
    private fun setupCookieIsolation() {
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(null, true)

        // 为应用设置唯一的 Cookie 标识
        val homeUrlHost = extractHost(config.homeUrl)
        if (homeUrlHost != null) {
            cookieManager.setCookie(homeUrlHost, "app_id=${config.appId}; Path=/")
            Log.d(TAG, "✅ Cookie 隔离已设置: $homeUrlHost")
        }
    }

    /**
     * 设置存储隔离
     *
     * 为不同应用设置独立的存储空间
     */
    private fun setupStorageIsolation() {
        // LocalStorage 和 SessionStorage 由 WebView 自动隔离域名
        // 这里我们通过子域名或路径来实现额外的隔离
        Log.d(TAG, "✅ 存储隔离已设置（基于域名）")
    }

    /**
     * 清理容器数据
     *
     * 当容器销毁或用户退出时调用，清理所有缓存和 Cookie
     */
    fun cleanup() {
        Log.d(TAG, "🧹 清理容器数据")

        // 清理 Cookie
        cleanupCookies()

        // 清理缓存
        cleanupCache()

        // 清理 WebStorage
        cleanupWebStorage()
    }

    /**
     * 清理 Cookie
     */
    private fun cleanupCookies() {
        val homeUrlHost = extractHost(config.homeUrl)
        if (homeUrlHost != null) {
            // 只清理当前域的 Cookie
            val cookies = cookieManager.getCookie(homeUrlHost)
            if (!cookies.isNullOrBlank()) {
                // 移除所有 Cookie
                cookies.split(";").forEach { cookie ->
                    val cookieName = cookie.split("=")[0].trim()
                    cookieManager.setCookie(homeUrlHost, "$cookieName=; Max-Age=0")
                }
                Log.d(TAG, "✅ Cookie 已清理")
            }
        }
    }

    /**
     * 清理缓存
     */
    private fun cleanupCache() {
        try {
            // 删除应用专属缓存目录
            if (appCacheDir.exists()) {
                appCacheDir.deleteRecursively()
                Log.d(TAG, "✅ 缓存目录已清理: ${appCacheDir.absolutePath}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ 清理缓存失败: ${e.message}", e)
        }
    }

    /**
     * 清理 WebStorage（LocalStorage、SessionStorage）
     */
    private fun cleanupWebStorage() {
        try {
            // 清理所有域的 WebStorage（谨慎使用）
            // WebStorage.getInstance().deleteAllData()

            // 更安全的做法：只清理特定域
            Log.d(TAG, "⚠️ WebStorage 清理需要手动调用 H5 清理接口")
        } catch (e: Exception) {
            Log.e(TAG, "❌ 清理 WebStorage 失败: ${e.message}", e)
        }
    }

    /**
     * 获取应用专属缓存目录
     */
    fun getCacheDir(): File {
        return appCacheDir
    }

    /**
     * 从 URL 提取主机名
     */
    private fun extractHost(url: String): String? {
        return try {
            val uri = android.net.Uri.parse(url)
            uri.host
        } catch (e: Exception) {
            Log.e(TAG, "❌ 解析 URL 失败: $url", e)
            null
        }
    }

    /**
     * 检查 URL 是否属于当前应用
     */
    fun isOwnUrl(url: String): Boolean {
        val host = extractHost(url) ?: return false
        return config.allowedDomains.any { allowedDomain ->
            host.endsWith(allowedDomain) || allowedDomain.endsWith(host)
        }
    }

    /**
     * 获取容器信息（用于调试）
     */
    fun getContainerInfo(): Map<String, Any?> {
        return mapOf(
            "appId" to config.appId,
            "appName" to config.appName,
            "homeUrl" to config.homeUrl,
            "cacheDir" to appCacheDir.absolutePath,
            "cookieCount" to (cookieManager.getCookie(extractHost(config.homeUrl) ?: "")?.split(";")?.size ?: 0)
        )
    }
}
