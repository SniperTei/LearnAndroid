package com.sniper.webbox.user.model

/**
 * 登录响应数据模型
 * 根据登录API返回的data字段结构定义
 */
data class LoginResponseData(
    val access_token: String,  // 生成的JWT令牌
    val token_type: String     // 令牌类型，如bearer
)
