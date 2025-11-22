package com.sniper.webbox.network.api

import com.sniper.webbox.network.api.ApiResponse
import com.sniper.webbox.network.enums.ParamType
import com.sniper.webbox.network.enums.RequestMethod
import com.sniper.webbox.user.model.LoginResponseData
import com.sniper.webbox.user.model.RegisterResponseData
import com.sniper.webbox.user.model.UserInfo

/**
 * 用户相关API服务
 */
class UserApi : ApiService() {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录响应
     */
    suspend fun login(username: String, password: String): ApiResponse<LoginResponseData> {
        val request = LoginRequest(username, password)
        return executeAndParse(request, LoginResponseData::class.java)
    }

    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @return 注册响应
     */
    suspend fun register(username: String, password: String, email: String): ApiResponse<RegisterResponseData> {
        val request = RegisterRequest(username, password, email)
        return executeAndParse(request, RegisterResponseData::class.java)
    }

    /**
     * 获取用户信息
     * @return 用户信息响应
     */
    suspend fun getUserInfo(): ApiResponse<UserInfo> {
        val request = GetUserInfoRequest()
        return executeAndParse(request, UserInfo::class.java)
    }
}

// 登录请求
class LoginRequest(private val identifier: String, private val password: String) : ApiRequest {
    override fun getApiPath(): String = "/api/v1/users/login"
    override fun getRequestMethod(): RequestMethod = RequestMethod.POST
    override fun getParamType(): ParamType = ParamType.MAP
    override fun getParams(): Any = mapOf(
        "identifier" to identifier,
        "password" to password
    )
    
    // 登录接口不需要认证
    override fun needAuth(): Boolean = false
}

// 注册请求
class RegisterRequest(
    private val username: String,
    private val password: String,
    private val email: String? = null,
    private val mobile: String? = null
) : ApiRequest {
    override fun getApiPath(): String = "/api/v1/users/"
    override fun getRequestMethod(): RequestMethod = RequestMethod.POST
    override fun getParamType(): ParamType = ParamType.MAP
    override fun getParams(): Any {
        val params = mutableMapOf(
            "username" to username,
            "password" to password
        )
        
        // 根据接口文档要求，email和mobile至少提供一个
        email?.takeIf { it.isNotEmpty() }?.let { params["email"] = it }
        mobile?.takeIf { it.isNotEmpty() }?.let { params["mobile"] = it }
        
        return params
    }
}

// 获取用户信息请求
// 注意：此接口需要Bearer Token认证
class GetUserInfoRequest : ApiRequest {
    override fun getApiPath(): String = "/api/v1/users/me"
    override fun getRequestMethod(): RequestMethod = RequestMethod.GET
    override fun getParamType(): ParamType = ParamType.NONE
    override fun getParams(): Any? = null
}

// 类型别名，保持API接口使用的一致性
// 登录响应数据类型别名，直接使用LoginResponseData
// 用户信息响应数据类型别名，直接使用UserInfo

// 注册响应数据类与UserInfoResponse相同，使用同一个数据类即可