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
    fun success(code: String = "000000", msg: String = "success", data: Any? = null) {
        invokeCallback(code, msg, data)
    }
    
    /**
     * 失败回调
     * @param code 错误码
     * @param msg 错误信息
     * @param data 失败时的附加数据
     */
    fun error(code: String = "999999", msg: String = "error", data: Any? = null) {
        invokeCallback(code, msg, data)
    }
    
    /**
     * 调用H5回调函数
     * @param code 状态码
     * @param msg 状态信息
     * @param data 要返回的数据
     */
    private fun invokeCallback(code: String = "000000", msg: String = "success", data: Any? = null) {
        if (callbackId.isNotEmpty()) {
            try {
                // 将结果对象序列化为JSON字符串
                val resultJson = gson.toJson(data)
                
                // 在主线程执行JavaScript，直接调用回调函数，传递完整的结果对象
                // 注意：code和msg需要用引号包裹，否则会被JavaScript当作变量名解析
                webView.post {
                    val jsCall = "window.$callbackId('$code', '$msg', $resultJson)"
                    webView.evaluateJavascript(jsCall, null)
                }
            } catch (e: Exception) {
                // 处理JSON序列化或JavaScript执行错误
                e.printStackTrace()
            }
        }
    }
}