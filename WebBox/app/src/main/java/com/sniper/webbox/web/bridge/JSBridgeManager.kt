package com.sniper.webbox.web.bridge

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.sniper.webbox.web.bridge.callback.JSCallback
import com.sniper.webbox.web.bridge.handler.JSHandler

class JSBridgeManager private constructor() {
    // 存储 模块名 与 handler 的映射（如 "device" → DeviceHandler()）
    private val handlerMap = mutableMapOf<String, JSHandler>()
    private val mainHandler = Handler(Looper.getMainLooper())
    
    companion object {
        val instance by lazy { JSBridgeManager() }
    }
    
    /**
     * 注册处理器
     * @param handler 处理器实例，会自动获取模块名
     */
    fun registerHandler(handler: JSHandler) {
        val moduleName = handler.getModuleName()
        handlerMap[moduleName] = handler
    }
    
    /**
     * 根据模块名获取处理器
     * @param moduleName 模块名
     * @return 处理器实例
     */
    fun getHandler(moduleName: String): JSHandler? {
        return handlerMap[moduleName]
    }
    
    /**
     * 处理JS调用
     * @param method 方法名，格式为"模块名.方法名"（如 "device.getDeviceInfo"）
     * @param params 参数
     * @param callback 回调接口，用于将结果返回给调用方（H5）
     */
    fun handle(method: String, params: Map<String, Any>?, callback: JSCallback?) {
        try {
            // 解析方法名，格式为"模块名.方法名"
            val parts = method.split(".")
            if (parts.size != 2) {
                // callback?.error(400, "方法格式错误，请使用'模块名.方法名'格式")
                callback?.error("900400", "方法格式错误，请使用'模块名.方法名'格式")
                return
            }
            
            val moduleName = parts[0]
            val functionName = parts[1]
            
            // 获取对应的处理器
            val handler = getHandler(moduleName)
            if (handler == null) {
                // callback?.error(404, "未找到模块: $moduleName")
                callback?.error("900404", "未找到模块: $moduleName")
                return
            }
            Log.d("JSBridgeManager", "handle: $moduleName.$functionName")

            // 将paramsMap转换为JSON字符串
            val paramsJson = if (params != null) {
                Gson().toJson(params)
            } else {
                ""
            }
            
            // 调用处理器的handle方法，并传入回调函数
            // 处理器执行完成后，会调用我们提供的回调函数，然后我们再通过JSCallback回调给H5
            handler.handle(functionName, paramsJson) { code, msg, data ->
                // 确保在主线程执行回调，因为evaluateJavascript需要在主线程调用
                mainHandler.post {
                    try {
                        // 成功回调给H5，传递正确的参数
                        callback?.success(code, msg, data)
                    } catch (e: Exception) {
                        // 回调过程中出错
                        callback?.error("900500", "回调处理异常: ${e.message}")
                    }
                }
            }
            
        } catch (e: Exception) {
            // 确保在主线程执行错误回调
            mainHandler.post {
                // callback?.error(500, "处理异常: ${e.message}")
                callback?.error("900500", "处理异常: ${e.message}")
            }
        }
    }
    
    /**
     * 处理JS调用（字符串参数版本）
     * @param method 方法名
     * @param params 参数字符串
     * @param callback 回调接口
     */
    fun handle(method: String, params: String, callback: JSCallback?) {
        try {
            Log.d("JSBridgeManager", "handle string params: $method, params: $params")
            // 尝试解析参数字符串为Map
            val paramsMap = if (params.isNotEmpty() && params != "null") {
                Gson().fromJson(params, Map::class.java) as? Map<String, Any>
            } else {
                null
            }
            handle(method, paramsMap, callback)
        } catch (e: Exception) {
            Log.e("JSBridgeManager", "parse params error: ${e.message}")
            callback?.error("900001", "参数解析失败: ${e.message}")
        }
    }
}