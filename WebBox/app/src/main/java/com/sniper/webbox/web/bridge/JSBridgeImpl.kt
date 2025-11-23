package com.sniper.webbox.web.bridge

import android.webkit.WebView
import com.sniper.webbox.web.activity.WebActivity
import com.sniper.webbox.web.bridge.callback.JSCallback

class JSBridgeImpl(
    private val activity: WebActivity,
    private val webView: WebView
) : JSBridgeInterface {
    private val bridgeManager = JSBridgeManager.instance
    
    override fun callNative(method: String, params: String, callback: String) {
        activity.runOnUiThread {
            try {
                // 创建JS回调对象
                val jsCallback = JSCallback(webView, callback)
                
                // 调用JSBridgeManager处理JS调用
                bridgeManager.handle(method, params, jsCallback)
            } catch (e: Exception) {
                // 创建JS回调对象
                val jsCallback = JSCallback(webView, callback)
                // 处理异常
                jsCallback.error("900001", "调用原生方法失败: ${e.message}")
            }
        }
    }
    
    override fun callJs(callbackId: String, result: String) {
        if (callbackId.isNotEmpty()) {
            webView.evaluateJavascript("window.$callbackId($result)", null)
        }
    }
}