package com.sniper.webbox.web.bridge.handler

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * 网络状态 Handler
 *
 * 提供以下功能：
 * - getNetworkType: 获取网络类型（wifi/4g/none）
 * - isNetworkAvailable: 检查网络是否可用
 * - watchNetworkStatus: 监听网络状态变化
 */
class NetworkHandler(private val context: Context) : JSHandler {

    private val TAG = "NetworkHandler"
    private val mainHandler = Handler(Looper.getMainLooper())

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    // 网络状态监听器
    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    override fun getModuleName(): String {
        return "network"
    }

    override fun handle(
        functionName: String,
        params: String,
        callback: (String, String, Any?) -> Unit
    ) {
        when (functionName) {
            "getNetworkType" -> getNetworkType(callback)
            "isNetworkAvailable" -> isNetworkAvailable(callback)
            "watchNetworkStatus" -> watchNetworkStatus(callback)
            "stopWatch" -> stopWatch(callback)
            else -> {
                callback("100001", "Unknown function: $functionName", null)
            }
        }
    }

    /**
     * 获取网络类型
     */
    private fun getNetworkType(callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                val networkType = getCurrentNetworkType()
                val isAvailable = isNetworkConnected()

                callback("000000", "success", mapOf(
                    "type" to networkType,
                    "available" to isAvailable
                ))
                Log.d(TAG, "✅ Network type: $networkType, available: $isAvailable")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Get network type error: ${e.message}", e)
                callback("900001", "获取网络类型失败: ${e.message}", null)
            }
        }
    }

    /**
     * 检查网络是否可用
     */
    private fun isNetworkAvailable(callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                val isAvailable = isNetworkConnected()

                callback("000000", "success", mapOf(
                    "available" to isAvailable
                ))
                Log.d(TAG, "✅ Network available: $isAvailable")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Check network available error: ${e.message}", e)
                callback("900001", "检查网络状态失败: ${e.message}", null)
            }
        }
    }

    /**
     * 监听网络状态变化
     */
    private fun watchNetworkStatus(callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                // 先停止之前的监听
                stopWatchNetwork()

                // 创建网络监听器
                networkCallback = object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        val networkType = getCurrentNetworkType()
                        Log.d(TAG, "🌐 Network available: $networkType")
                        // 实际项目应通过 JSCallback 回调 H5
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        Log.d(TAG, "🌐 Network lost")
                        // 实际项目应通过 JSCallback 回调 H5
                    }

                    override fun onCapabilitiesChanged(
                        network: Network,
                        networkCapabilities: NetworkCapabilities
                    ) {
                        super.onCapabilitiesChanged(network, networkCapabilities)
                        val networkType = getCurrentNetworkType()
                        Log.d(TAG, "🌐 Network capabilities changed: $networkType")
                    }
                }

                // 注册网络监听
                val networkRequest = NetworkRequest.Builder().build()
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback!!)

                callback("000000", "网络监听已启动", mapOf(
                    "watching" to true,
                    "currentType" to getCurrentNetworkType()
                ))
                Log.d(TAG, "✅ Network status watch started")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Watch network status error: ${e.message}", e)
                callback("900001", "启动网络监听失败: ${e.message}", null)
            }
        }
    }

    /**
     * 停止监听
     */
    private fun stopWatch(callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                stopWatchNetwork()
                callback("000000", "网络监听已停止", null)
                Log.d(TAG, "✅ Network status watch stopped")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Stop watch error: ${e.message}", e)
                callback("900001", "停止监听失败: ${e.message}", null)
            }
        }
    }

    /**
     * 停止网络监听（内部方法）
     */
    private fun stopWatchNetwork() {
        networkCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
            networkCallback = null
        }
    }

    /**
     * 获取当前网络类型
     */
    private fun getCurrentNetworkType(): String {
        if (!isNetworkConnected()) return "none"

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        return when {
            capabilities == null -> "none"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "wifi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "cellular"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ethernet"
            else -> "unknown"
        }
    }

    /**
     * 检查网络是否已连接
     */
    private fun isNetworkConnected(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            return capabilities != null &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }

    /**
     * 清理资源
     */
    fun cleanup() {
        stopWatchNetwork()
        Log.d(TAG, "🧹 NetworkHandler cleaned up")
    }
}
