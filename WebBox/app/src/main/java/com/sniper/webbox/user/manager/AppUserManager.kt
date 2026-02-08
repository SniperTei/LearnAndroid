package com.sniper.webbox.user.manager

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.sniper.webbox.user.model.UserInfo
import androidx.core.content.edit

/**
 * 用户管理单例
 * 负责用户信息的存储、获取、更新和清除
 */
object AppUserManager {
    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER_INFO = "user_info"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_TOKEN_TYPE = "token_type"
    private const val KEY_LOGIN_STATUS = "login_status"

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var gson: Gson

    // 用户信息缓存
    private var userInfo: UserInfo? = null
    private var authToken: String? = null
    private var tokenType: String? = null
    private var isLoggedIn = false

    /**
     * 初始化方法
     */
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        gson = Gson()
        // 从存储中加载用户信息
        loadUserData()
    }

    /**
     * 登录成功后保存认证信息
     * 注意：此时只保存token，用户详细信息需要后续调用用户信息接口获取
     */
    fun saveAuthInfo(accessToken: String, tokenType: String) {
        this.authToken = accessToken
        this.tokenType = tokenType
        this.isLoggedIn = true

        // 持久化存储认证信息
        sharedPreferences.edit {
            putString(KEY_TOKEN, accessToken)
                .putString(KEY_TOKEN_TYPE, tokenType)
                .putBoolean(KEY_LOGIN_STATUS, true)
        }
    }

    /**
     * 保存用户详细信息
     */
    fun saveUserInfo(userData: UserInfo) {
        this.userInfo = userData

        // 持久化存储用户信息
        sharedPreferences.edit()
            .putString(KEY_USER_INFO, gson.toJson(userData))
            .apply()
    }

    /**
     * 退出登录
     */
    fun logout() {
        this.authToken = null
        this.tokenType = null
        this.userInfo = null
        this.isLoggedIn = false

        // 清除存储
        sharedPreferences.edit()
            .remove(KEY_TOKEN)
            .remove(KEY_TOKEN_TYPE)
            .remove(KEY_USER_INFO)
            .putBoolean(KEY_LOGIN_STATUS, false)
            .apply()
    }

    /**
     * 获取完整的认证Token（包含tokenType）
     */
    fun getFullAuthToken(): String? {
        return if (authToken != null && tokenType != null) {
            "$tokenType $authToken"
        } else {
            null
        }
    }

    /**
     * 获取认证Token
     */
    fun getAuthToken(): String? = authToken

    /**
     * 获取Token类型
     */
    fun getTokenType(): String? = tokenType

    /**
     * 获取用户信息
     */
    fun getUserInfo(): UserInfo? = userInfo

    /**
     * 检查是否已登录
     */
    fun isLoggedIn(): Boolean = isLoggedIn

    /**
     * 更新用户信息
     */
    fun updateUserInfo(newInfo: UserInfo) {
        saveUserInfo(newInfo)
    }

    /**
     * 从存储加载用户数据
     */
    private fun loadUserData() {
        isLoggedIn = sharedPreferences.getBoolean(KEY_LOGIN_STATUS, false)
        authToken = sharedPreferences.getString(KEY_TOKEN, null)
        tokenType = sharedPreferences.getString(KEY_TOKEN_TYPE, null)

        val userJson = sharedPreferences.getString(KEY_USER_INFO, null)
        if (userJson != null) {
            userInfo = try {
                gson.fromJson(userJson, UserInfo::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    /**
     * 获取用于H5交互的用户信息
     * 返回Map对象，让JSCallback统一序列化，避免双重序列化问题
     */
    fun getUserInfoForH5(): Map<String, Any?> {
        return mapOf(
            "token" to authToken,
            "tokenType" to tokenType,
            "userInfo" to userInfo,
            "isLoggedIn" to isLoggedIn
        )
    }
}