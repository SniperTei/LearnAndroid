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
     * 直接供H5调用的接口，接收一个JSON字符串参数
     */
    @JavascriptInterface
    override fun callNative(data: String) {
        activity.runOnUiThread {
            Log.d("JSBridgeImpl", "callNative data: $data")
            try {
                // 解析JS传来的调用数据
                val jsonObject = JSONObject(data)
                val method = jsonObject.optString("method", "")
                val params = jsonObject.optString("params", "{}")
                val callbackId = jsonObject.optString("callbackId", "")
                
                Log.d("JSBridgeImpl", "Parsed - method: $method, params: $params, callback: $callbackId")
                
                // 创建JS回调对象
                val jsCallback = JSCallback(webView, callbackId)
                
                // 调用JSBridgeManager处理JS调用
                bridgeManager.handle(method, params, jsCallback)
            } catch (e: Exception) {
                Log.e("JSBridgeImpl", "callNative error: ${e.message}")
                
                // 尝试解析回调ID，以便返回错误信息
                var callbackId = ""
                try {
                    val jsonObject = JSONObject(data)
                    callbackId = jsonObject.optString("callbackId", "")
                } catch (innerE: Exception) {
                    // 忽略解析错误
                }
                
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