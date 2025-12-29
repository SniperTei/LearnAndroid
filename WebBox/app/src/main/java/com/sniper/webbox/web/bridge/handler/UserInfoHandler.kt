package com.sniper.webbox.web.bridge.handler

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
import com.sniper.webbox.user.activity.LoginActivity
import com.sniper.webbox.user.manager.AppUserManager

/**
 * 用户信息处理器
 * 负责处理H5对用户信息的请求
 */
class UserInfoHandler(private val context: Context) : JSHandler {
    private val gson = Gson()
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun getModuleName(): String {
        return "userInfo"
    }

    override fun handle(
        functionName: String,
        params: String,
        callback: (code: String, msg: String, data: Any?) -> Unit
    ) {
        Log.d("UserInfoHandler", "handle function: $functionName, params: $params")

        when (functionName) {
            "getUserInfoFromApp" -> {
                // 从AppUserManager获取用户信息
                val userInfo = AppUserManager.getUserInfoForH5()
                
                // 确保在主线程回调
                mainHandler.post {
                    callback("000000", "success", userInfo)
                }
            }
            "logout" -> {
                Log.d("UserInfoHandler", "logout function called")
                // 调用AppUserManager的logout方法
                AppUserManager.logout()
                // 返回到登录页 LoginActivity
                val intent = Intent(context, LoginActivity::class.java)
//                val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
//                intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                finishCurrentActivity()
            }
            else -> {
                // 不支持的方法
                mainHandler.post {
                    callback("900001", "不支持的方法: $functionName", null)
                }
            }
        }
    }

    // 辅助方法：关闭当前Activity
    private fun finishCurrentActivity() {
        (context as? android.app.Activity)?.finish()
    }
}