package com.sniper.webbox.web.bridge.callback

import android.webkit.WebView
import com.google.gson.Gson

/**
 * JS回调封装类，用于将原生处理结果返回给H5页面
 * 遵循"谁调用，回调给谁"的原则
 */
class JSCallback(private val webView: WebView, private val callbackId: String) {
    private val gson = Gson()
    
    /**
     * 成功回调
     * @param data 要返回的数据，可以是任意类型，会被自动转换为JSON
     */
    fun success(data: Any?) {
        val result = mapOf(
            "code" to "000000",
            "msg" to "success",
            "data" to data
        )
        invokeCallback(result)
    }
    
    /**
     * 失败回调
     * @param code 错误码
     * @param message 错误信息
     */
    fun error(code: String = "900009", message: String = "操作失败") {
        val result = mapOf(
            "code" to code,
            "msg" to message,
            "data" to null
        )
        invokeCallback(result)
    }
    
    /**
     * 调用H5回调函数
     * @param result 要返回的数据
     */
    private fun invokeCallback(result: Map<String, Any?>) {
        if (callbackId.isNotEmpty()) {
            try {
                // 将结果转换为JSON字符串
                val jsonResult = gson.toJson(result)
                
                // 在主线程执行JavaScript
                webView.post {
                    webView.evaluateJavascript("window.$callbackId($jsonResult)", null)
                }
            } catch (e: Exception) {
                // 处理JSON序列化或JavaScript执行错误
                e.printStackTrace()
            }
        }
    }
}