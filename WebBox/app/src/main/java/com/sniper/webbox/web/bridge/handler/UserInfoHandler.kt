package com.sniper.webbox.web.bridge.handler

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.gson.Gson
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
            else -> {
                // 不支持的方法
                mainHandler.post {
                    callback("900001", "不支持的方法: $functionName", null)
                }
            }
        }
    }
}