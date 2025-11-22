package com.sniper.webbox.network.enums

enum class ParamType {
    MAP, // Map<String, Any> 格式
    JSON_STRING, // JSON 字符串
    FORM_BODY, // 表单格式（FormBody）
    NONE // 无参数
}