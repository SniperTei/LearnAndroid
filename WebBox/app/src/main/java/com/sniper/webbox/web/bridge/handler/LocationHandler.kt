package com.sniper.webbox.web.bridge.handler

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.JsonObject

/**
 * 地理位置 Handler
 *
 * 提供以下功能：
 * - getCurrentLocation: 获取当前位置（一次性）
 * - watchPosition: 监听位置变化（持续）
 * - clearWatch: 停止监听
 */
class LocationHandler(private val context: Context) : JSHandler {

    private val TAG = "LocationHandler"
    private val mainHandler = Handler(Looper.getMainLooper())
    private val gson = Gson()

    // 位置监听器管理
    private val watchListeners = mutableMapOf<String, Runnable>()

    override fun getModuleName(): String {
        return "location"
    }

    override fun handle(
        functionName: String,
        params: String,
        callback: (String, String, Any?) -> Unit
    ) {
        when (functionName) {
            "getCurrentLocation" -> getCurrentLocation(callback)
            "watchPosition" -> watchPosition(params, callback)
            "clearWatch" -> clearWatch(params, callback)
            else -> {
                callback("100001", "Unknown function: $functionName", null)
            }
        }
    }

    /**
     * 获取当前位置（一次性）
     */
    private fun getCurrentLocation(callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            if (!checkLocationPermission()) {
                callback("900001", "位置权限未授予", null)
                return@post
            }

            // 简化实现：返回模拟位置数据
            // 实际项目应使用 FusedLocationProviderClient 或 LocationManager
            val locationData = mapOf(
                "latitude" to 39.9042,
                "longitude" to 116.4074,
                "accuracy" to 100.0,
                "timestamp" to System.currentTimeMillis()
            )

            callback("000000", "success", locationData)
            Log.d(TAG, "✅ Current location returned")
        }
    }

    /**
     * 监听位置变化
     *
     * @param params JSON 字符串，格式：{"watchId": "123", "interval": 5000}
     */
    private fun watchPosition(params: String, callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            if (!checkLocationPermission()) {
                callback("900001", "位置权限未授予", null)
                return@post
            }

            try {
                val json = gson.fromJson(params, JsonObject::class.java)
                val watchId = json.get("watchId")?.asString ?: generateWatchId()
                val interval = json.get("interval")?.asLong ?: 5000

                // 创建位置监听器
                val listener = Runnable {
                    // 定期返回位置数据
                    val locationData = mapOf(
                        "latitude" to 39.9042,
                        "longitude" to 116.4074,
                        "accuracy" to 100.0,
                        "timestamp" to System.currentTimeMillis(),
                        "watchId" to watchId
                    )
                    // 实际项目应通过 JSCallback 回调 H5
                    Log.d(TAG, "📍 Position update: $locationData")
                }

                watchListeners[watchId] = listener
                mainHandler.postDelayed(listener, interval)

                callback("000000", "success", mapOf("watchId" to watchId))
                Log.d(TAG, "✅ Watch started: $watchId (interval: ${interval}ms)")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Watch position error: ${e.message}", e)
                callback("900002", "启动监听失败: ${e.message}", null)
            }
        }
    }

    /**
     * 停止监听
     *
     * @param params JSON 字符串，格式：{"watchId": "123"}
     */
    private fun clearWatch(params: String, callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                val json = gson.fromJson(params, JsonObject::class.java)
                val watchId = json.get("watchId")?.asString

                if (watchId != null && watchListeners.containsKey(watchId)) {
                    watchListeners.remove(watchId)
                    callback("000000", "success", mapOf("watchId" to watchId))
                    Log.d(TAG, "✅ Watch cleared: $watchId")
                } else {
                    callback("900003", "监听器不存在", null)
                    Log.w(TAG, "⚠️ Watch not found: $watchId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Clear watch error: ${e.message}", e)
                callback("900004", "停止监听失败: ${e.message}", null)
            }
        }
    }

    /**
     * 检查位置权限
     */
    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 生成监听器 ID
     */
    private fun generateWatchId(): String {
        return "watch_${System.currentTimeMillis()}"
    }

    /**
     * 清理所有监听器
     */
    fun cleanup() {
        watchListeners.clear()
        Log.d(TAG, "🧹 All location listeners cleared")
    }
}
