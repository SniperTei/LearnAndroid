package com.sniper.webbox.web.bridge

import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import org.json.JSONObject
import com.sniper.webbox.web.activity.WebActivity
import com.sniper.webbox.web.bridge.callback.JSCallback

class JSBridgeImpl(
    private val activity: WebActivity,
    private val webView: WebView
) : JSBridgeInterface {
    private val bridgeManager = JSBridgeManager.instance
    
    /**
     * 直接供H5调用的接口，接收method、params和callbackId三个参数
     */
    @JavascriptInterface
    override fun callNative(method: String, params: String, callbackId: String) {
        activity.runOnUiThread {
            Log.d("JSBridgeImpl", "callNative - method: $method, params: $params, callbackId: $callbackId")
            try {
                // 创建JS回调对象
                val jsCallback = JSCallback(webView, callbackId)
                
                // 调用JSBridgeManager处理JS调用
                bridgeManager.handle(method, params, jsCallback)
            } catch (e: Exception) {
                Log.e("JSBridgeImpl", "callNative error: ${e.message}")
                
                // 创建JS回调对象
                val jsCallback = JSCallback(webView, callbackId)
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