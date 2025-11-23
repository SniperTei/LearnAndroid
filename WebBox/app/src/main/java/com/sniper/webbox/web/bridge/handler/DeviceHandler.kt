package com.sniper.webbox.web.bridge.handler

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.sniper.webbox.web.bridge.callback.JSCallback

class DeviceHandler(private val context: Context) : JSHandler {
    private val gson = Gson()
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun getModuleName(): String {
        return "device"
    }
    
    override fun handle(params: String, callback: (String) -> Unit) {
        when (params) {
            "getDeviceInfo" -> {
                // 获取设备信息并通过callback返回
                val deviceInfo = getDeviceInfo()
                val jsonResult = gson.toJson(deviceInfo)
                callback(jsonResult)
            }
            "doSomethingCostVeryLongTime" -> {
                // 耗时操作放在子线程执行，避免阻塞主线程
                Thread {
                    try {
                        // 模拟耗时操作
                        Thread.sleep(3000)
                        
                        // 确保在主线程回调，因为涉及UI交互
                        mainHandler.post {
                            callback("{\"message\": \"耗时操作完成\", \"status\": \"success\"}")
                        }
                    } catch (e: Exception) {
                        mainHandler.post {
                            callback("{\"message\": \"操作失败: ${e.message}\", \"status\": \"error\"}")
                        }
                    }
                }.start()
            }
            else -> {
                callback("{\"message\": \"不支持的方法: $params\", \"status\": \"error\"}")
            }
        }
    }

    private fun getDeviceInfo(): Map<String, String> {
        return mapOf(
            "androidVersion" to Build.VERSION.RELEASE,
            "model" to Build.MODEL,
            "brand" to Build.BRAND,
            "manufacturer" to Build.MANUFACTURER,
            "device" to Build.DEVICE,
            "id" to Build.ID,
            "type" to Build.TYPE,
            "hardware" to Build.HARDWARE,
            "product" to Build.PRODUCT,
            "serial" to Build.SERIAL
        )
    }
}