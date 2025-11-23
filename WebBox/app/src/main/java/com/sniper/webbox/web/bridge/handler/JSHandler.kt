package com.sniper.webbox.web.bridge.handler

interface JSHandler {

    /**
     * 获取模块名称
     * @return 模块名称
     */
    fun getModuleName(): String

    /**
     * 处理 JS 调用
     * @param params H5 传入的参数（JSON 字符串）
     * @param callback 处理完成后调用，传入结果（JSON 字符串）
     */
    fun handle(params: String, callback: (String) -> Unit)
}