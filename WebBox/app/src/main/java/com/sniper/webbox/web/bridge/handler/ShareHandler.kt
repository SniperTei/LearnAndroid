package com.sniper.webbox.web.bridge.handler

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject

/**
 * 分享 Handler
 *
 * 提供以下功能：
 * - shareToWeChat: 分享到微信
 * - shareToMoments: 分享到朋友圈
 * - shareToQQ: 分享到 QQ
 * - shareToWeibo: 分享到微博
 * - shareToSystem: 使用系统分享
 */
class ShareHandler(private val context: Context) : JSHandler {

    private val TAG = "ShareHandler"
    private val mainHandler = Handler(Looper.getMainLooper())
    private val gson = Gson()

    override fun getModuleName(): String {
        return "share"
    }

    override fun handle(
        functionName: String,
        params: String,
        callback: (String, String, Any?) -> Unit
    ) {
        when (functionName) {
            "shareToWeChat" -> shareToWeChat(params, callback)
            "shareToMoments" -> shareToMoments(params, callback)
            "shareToQQ" -> shareToQQ(params, callback)
            "shareToWeibo" -> shareToWeibo(params, callback)
            "shareToSystem" -> shareToSystem(params, callback)
            "checkInstalled" -> checkInstalled(params, callback)
            else -> {
                callback("100001", "Unknown function: $functionName", null)
            }
        }
    }

    /**
     * 分享到微信
     *
     * @param params JSON 字符串，格式：{"title": "标题", "description": "描述", "url": "链接", "imageUrl": "图片URL"}
     */
    private fun shareToWeChat(params: String, callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                val json = gson.fromJson(params, JsonObject::class.java)
                val title = json.get("title")?.asString ?: ""
                val description = json.get("description")?.asString ?: ""
                val url = json.get("url")?.asString ?: ""
                val imageUrl = json.get("imageUrl")?.asString

                // 实际项目需要集成微信 SDK
                // 这里使用模拟实现
                Log.d(TAG, "📤 Share to WeChat: $title - $description")

                callback("000000", "分享成功", mapOf(
                    "platform" to "wechat",
                    "title" to title,
                    "url" to url
                ))
            } catch (e: Exception) {
                Log.e(TAG, "❌ Share to WeChat error: ${e.message}", e)
                callback("900001", "分享失败: ${e.message}", null)
            }
        }
    }

    /**
     * 分享到朋友圈
     */
    private fun shareToMoments(params: String, callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                val json = gson.fromJson(params, JsonObject::class.java)
                val title = json.get("title")?.asString ?: ""
                val imageUrl = json.get("imageUrl")?.asString

                // 实际项目需要集成微信 SDK
                Log.d(TAG, "📤 Share to Moments: $title")

                callback("000000", "分享成功", mapOf(
                    "platform" to "moments",
                    "title" to title
                ))
            } catch (e: Exception) {
                Log.e(TAG, "❌ Share to Moments error: ${e.message}", e)
                callback("900001", "分享失败: ${e.message}", null)
            }
        }
    }

    /**
     * 分享到 QQ
     */
    private fun shareToQQ(params: String, callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                val json = gson.fromJson(params, JsonObject::class.java)
                val title = json.get("title")?.asString ?: ""
                val url = json.get("url")?.asString ?: ""
                val imageUrl = json.get("imageUrl")?.asString

                // 实际项目需要集成 QQ SDK
                Log.d(TAG, "📤 Share to QQ: $title")

                callback("000000", "分享成功", mapOf(
                    "platform" to "qq",
                    "title" to title,
                    "url" to url
                ))
            } catch (e: Exception) {
                Log.e(TAG, "❌ Share to QQ error: ${e.message}", e)
                callback("900001", "分享失败: ${e.message}", null)
            }
        }
    }

    /**
     * 分享到微博
     */
    private fun shareToWeibo(params: String, callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                val json = gson.fromJson(params, JsonObject::class.java)
                val text = json.get("text")?.asString ?: ""
                val imageUrl = json.get("imageUrl")?.asString

                // 实际项目需要集成微博 SDK
                Log.d(TAG, "📤 Share to Weibo: $text")

                callback("000000", "分享成功", mapOf(
                    "platform" to "weibo",
                    "text" to text
                ))
            } catch (e: Exception) {
                Log.e(TAG, "❌ Share to Weibo error: ${e.message}", e)
                callback("900001", "分享失败: ${e.message}", null)
            }
        }
    }

    /**
     * 使用系统分享
     *
     * @param params JSON 字符串，格式：{"title": "标题", "text": "文本", "url": "链接"}
     */
    private fun shareToSystem(params: String, callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                val json = gson.fromJson(params, JsonObject::class.java)
                val title = json.get("title")?.asString ?: ""
                val text = json.get("text")?.asString ?: ""
                val url = json.get("url")?.asString ?: ""

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, title)
                    putExtra(Intent.EXTRA_TEXT, "$text\n$url")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                // 创建分享选择器
                val chooserIntent = Intent.createChooser(shareIntent, "分享到").apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }

                context.startActivity(chooserIntent)

                callback("000000", "系统分享已启动", null)
                Log.d(TAG, "✅ System share started")
            } catch (e: Exception) {
                Log.e(TAG, "❌ System share error: ${e.message}", e)
                callback("900001", "启动分享失败: ${e.message}", null)
            }
        }
    }

    /**
     * 检查应用是否已安装
     *
     * @param params JSON 字符串，格式：{"platform": "wechat"}
     */
    private fun checkInstalled(params: String, callback: (String, String, Any?) -> Unit) {
        mainHandler.post {
            try {
                val json = gson.fromJson(params, JsonObject::class.java)
                val platform = json.get("platform")?.asString ?: ""

                val isInstalled = when (platform) {
                    "wechat", "moments" -> isAppInstalled("com.tencent.mm")
                    "qq" -> isAppInstalled("com.tencent.mobileqq")
                    "weibo" -> isAppInstalled("com.sina.weibo")
                    else -> false
                }

                callback("000000", "success", mapOf(
                    "platform" to platform,
                    "installed" to isInstalled
                ))
                Log.d(TAG, "✅ Check installed: $platform = $isInstalled")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Check installed error: ${e.message}", e)
                callback("900001", "检查失败: ${e.message}", null)
            }
        }
    }

    /**
     * 检查应用是否已安装
     */
    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }
}
