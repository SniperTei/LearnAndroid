package com.sniper.webbox.user.model

/**
 * 注册响应数据模型
 * 根据注册API返回的data字段结构定义
 * 注意：注册成功后直接返回token信息，可以立即进行自动登录
 */
data class RegisterResponseData(
    val access_token: String,  // 生成的JWT令牌
    val token_type: String     // 令牌类型，如bearer
)
