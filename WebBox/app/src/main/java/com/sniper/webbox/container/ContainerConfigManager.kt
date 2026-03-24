package com.sniper.webbox.container

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import java.io.IOException

/**
 * 容器配置管理器
 *
 * 负责加载、管理容器配置，支持多种配置来源：
 * 1. assets 目录的 JSON 文件（默认）
 * 2. 远程配置服务器（可选）
 * 3. 运行时动态设置（调试用）
 */
object ContainerConfigManager {

    private const val TAG = "ContainerConfigManager"
    private const val DEFAULT_CONFIG_FILE = "container_config.json"

    private var currentConfig: ContainerConfig? = null
    private val gson = Gson()

    /**
     * 从 assets 加载配置
     *
     * @param context 上下文
     * @param fileName 配置文件名（默认 container_config.json）
     * @return 是否加载成功
     */
    fun loadFromAssets(context: Context, fileName: String = DEFAULT_CONFIG_FILE): Boolean {
        return try {
            val json = loadJsonFromAssets(context, fileName)
            if (json.isNullOrBlank()) {
                Log.e(TAG, "配置文件为空: $fileName")
                return false
            }

            currentConfig = gson.fromJson(json, ContainerConfig::class.java)
            Log.d(TAG, "✅ 配置加载成功: ${currentConfig?.appName} (${currentConfig?.appId})")
            Log.d(TAG, "   首页 URL: ${currentConfig?.homeUrl}")
            Log.d(TAG, "   启用功能: ${currentConfig?.enabledFeatures?.joinToString()}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "❌ 配置加载失败: ${e.message}", e)
            false
        }
    }

    /**
     * 从 JSON 字符串加载配置
     *
     * @param jsonString JSON 配置字符串
     * @return 是否加载成功
     */
    fun loadFromJson(jsonString: String): Boolean {
        return try {
            currentConfig = gson.fromJson(jsonString, ContainerConfig::class.java)
            Log.d(TAG, "✅ 从 JSON 加载配置成功: ${currentConfig?.appName}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "❌ JSON 解析失败: ${e.message}", e)
            false
        }
    }

    /**
     * 直接设置配置（用于测试或动态配置）
     *
     * @param config 配置对象
     */
    fun setConfig(config: ContainerConfig) {
        currentConfig = config
        Log.d(TAG, "✅ 配置已设置: ${config.appName}")
    }

    /**
     * 获取当前配置
     *
     * @throws IllegalStateException 如果配置未加载
     */
    fun getConfig(): ContainerConfig {
        return currentConfig ?: throw IllegalStateException(
            "容器配置未加载，请先调用 loadFromAssets() 或 loadFromJson()"
        )
    }

    /**
     * 获取当前配置（安全版本，可能返回 null）
     */
    fun getConfigOrNull(): ContainerConfig? {
        return currentConfig
    }

    /**
     * 检查配置是否已加载
     */
    fun isConfigLoaded(): Boolean {
        return currentConfig != null
    }

    /**
     * 清除配置
     */
    fun clearConfig() {
        currentConfig = null
        Log.d(TAG, "🗑️ 配置已清除")
    }

    /**
     * 从 assets 读取 JSON 文件
     */
    private fun loadJsonFromAssets(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            Log.e(TAG, "读取 assets 文件失败: $fileName", e)
            null
        }
    }

    /**
     * 导出当前配置为 JSON
     */
    fun exportConfigAsJson(): String? {
        return try {
            currentConfig?.let { gson.toJson(it) }
        } catch (e: Exception) {
            Log.e(TAG, "导出配置失败: ${e.message}", e)
            null
        }
    }

    /**
     * 验证配置的有效性
     *
     * @return 验证结果和错误信息
     */
    fun validateConfig(): Pair<Boolean, String?> {
        val config = currentConfig ?: return false to "配置未加载"

        // 检查必填字段
        if (config.appId.isBlank()) {
            return false to "appId 不能为空"
        }

        if (config.appName.isBlank()) {
            return false to "appName 不能为空"
        }

        if (config.homeUrl.isBlank()) {
            return false to "homeUrl 不能为空"
        }

        // 检查 URL 格式
        if (!config.homeUrl.startsWith("http://") && !config.homeUrl.startsWith("https://")) {
            return false to "homeUrl 格式错误，必须以 http:// 或 https:// 开头"
        }

        // 检查启用的功能是否有效
        val validFeatures = AvailableFeatures.allFeatures()
        val invalidFeatures = config.enabledFeatures.filterNot { it in validFeatures }
        if (invalidFeatures.isNotEmpty()) {
            return false to "无效的功能: ${invalidFeatures.joinToString()}"
        }

        // 检查白名单域名格式
        val invalidDomains = config.allowedDomains.filter { domain ->
            !domain.matches(Regex("^[a-zA-Z0-9.-]+\$"))
        }
        if (invalidDomains.isNotEmpty()) {
            return false to "无效的域名: ${invalidDomains.joinToString()}"
        }

        return true to null
    }

    /**
     * 打印配置摘要（用于调试）
     */
    fun printConfigSummary() {
        val config = currentConfig ?: run {
            Log.d(TAG, "⚠️ 配置未加载")
            return
        }

        Log.d(TAG, "══════════════════════════════════════════════")
        Log.d(TAG, "📦 容器配置摘要")
        Log.d(TAG, "══════════════════════════════════════════════")
        Log.d(TAG, "应用 ID:     ${config.appId}")
        Log.d(TAG, "应用名称:    ${config.appName}")
        Log.d(TAG, "首页 URL:    ${config.homeUrl}")
        Log.d(TAG, "白名单域名:  ${config.allowedDomains.size} 个")
        Log.d(TAG, "启用功能:    ${config.enabledFeatures.size} 个")
        Log.d(TAG, "显示导航栏:  ${config.theme.showToolbar}")
        Log.d(TAG, "明文流量:    ${config.networkSecurity.allowCleartext}")
        Log.d(TAG, "══════════════════════════════════════════════")
    }
}
