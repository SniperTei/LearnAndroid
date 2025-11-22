package com.sniper.webbox.user.model

/**
 * 用户信息模型
 * 根据获取用户信息API返回的data字段结构定义
 */
data class UserInfo(
    val id: Int,              // 用户ID
    val email: String,        // 邮箱
    val username: String,     // 用户名
    val mobile: String,       // 手机号
    val is_active: Boolean,   // 是否激活
    val created_at: String,   // 创建时间
    val updated_at: String?   // 更新时间，可能为null
)
