package com.sniper.webbox.web.bridge.handler

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject

/**
 * 剪贴板 Handler
 *
 * 提供以下功能：
 * - setClipboard: 设置剪贴板内容
 * - getClipboard: 获取剪贴板内容
 * - clearClipboard: 清空剪贴板
 */
class ClipboardHandler(private val context: Context) : JSHandler {

    private val TAG = "ClipboardHandler"
    private val mainHandler = Handler(Looper.getMainLooper())
    private val gson = Gson()

    private val clipboardManager: ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    override fun getModuleName(): String {
        return "clipboard"
    }

    override fun handle(
        functionName: String,
        params: String,
        callback: (String, String, Any?) -> Unit
    ) {
        when (functionName) {
            "setClipboard" -> setClipboard(params, callback)
            "getClipboard" -> getClipboard(callback)
            "clearClipboard" -> clearClipboard(callback)
            else -> {
                callback("100001", "Unknown function: $functionName", null)
            }
        }
    }

    /**
     * 设置剪贴板内容
     *
     * @param params JSON 字符串，格式：{"text": "要复制的文本"}
     */
    private fun setClipboard(params: String, callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                val json = gson.fromJson(params, JsonObject::class.java)
                val text = json.get("text")?.asString

                if (text.isNullOrBlank()) {
                    callback("900001", "文本内容不能为空", null)
                    return@post
                }

                // 创建剪贴板数据
                val clipData = ClipData.newPlainText("WebBox", text)
                clipboardManager.setPrimaryClip(clipData)

                callback("000000", "复制成功", mapOf("text" to text))
                Log.d(TAG, "✅ Clipboard set: ${text.take(50)}...")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Set clipboard error: ${e.message}", e)
                callback("900002", "设置剪贴板失败: ${e.message}", null)
            }
        }
    }

    /**
     * 获取剪贴板内容
     */
    private fun getClipboard(callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                val clipData = clipboardManager.primaryClip

                if (clipData != null && clipData.itemCount > 0) {
                    val text = clipData.getItemAt(0).text?.toString()

                    if (text != null) {
                        callback("000000", "success", mapOf("text" to text))
                        Log.d(TAG, "✅ Clipboard get: ${text.take(50)}...")
                    } else {
                        callback("900003", "剪贴板为空", null)
                        Log.w(TAG, "⚠️ Clipboard is empty")
                    }
                } else {
                    callback("900003", "剪贴板为空", null)
                    Log.w(TAG, "⚠️ Clipboard is empty")
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Get clipboard error: ${e.message}", e)
                callback("900004", "获取剪贴板失败: ${e.message}", null)
            }
        }
    }

    /**
     * 清空剪贴板
     */
    private fun clearClipboard(callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                // 设置空剪贴板
                val clipData = ClipData.newPlainText("", "")
                clipboardManager.setPrimaryClip(clipData)

                callback("000000", "剪贴板已清空", null)
                Log.d(TAG, "✅ Clipboard cleared")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Clear clipboard error: ${e.message}", e)
                callback("900005", "清空剪贴板失败: ${e.message}", null)
            }
        }
    }
}
