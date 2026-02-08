package com.sniper.webbox.web.bridge.handler

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.sniper.webbox.web.bridge.callback.JSCallback

class DeviceHandler(private val context: Context) : JSHandler {
    private val gson = Gson()
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun getModuleName(): String {
        return "device"
    }
    
    override fun handle(functionName: String, params: String, callback: (code: String, msg: String, data: Any?) -> Unit) {
        when (functionName) {
            "getDeviceInfo" -> {
                // 获取设备信息并通过callback返回
                val deviceInfo = getDeviceInfo()
                callback("000000", "success", deviceInfo)
            }
            "finishActivity" -> {
                // 关闭当前Activity（用于退出登录等场景）
                Log.d("DeviceHandler", "finishActivity called")
                mainHandler.post {
                    (context as? android.app.Activity)?.finish()
                    callback("000000", "success", mapOf(
                        "message" to "Activity已关闭"
                    ))
                }
            }
            "doSomethingCostVeryLongTime" -> {
                // 耗时操作放在子线程执行，避免阻塞主线程
                Thread {
                    try {
                        // 模拟耗时操作
                        Thread.sleep(3000)

                        // 确保在主线程回调，因为涉及UI交互
                        mainHandler.post {
                            callback("000000", "success", mapOf(
                                "message" to "耗时操作完成",
                                "status" to "success"
                            ))
                        }
                    } catch (e: Exception) {
                        mainHandler.post {
                            callback("100009", "shibai", mapOf(
                                "message" to "操作失败: ${e.message}",
                                "status" to "error"
                            ))
                        }
                    }
                }.start()
            }
            else -> {
                callback("100009", "shibai", mapOf(
                    "message" to "不支持的方法: $functionName",
                    "status" to "error"
                ))
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