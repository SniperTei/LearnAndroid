package com.sniper.webbox.web.bridge.handler

interface JSHandler {

    /**
     * 获取模块名称
     * @return 模块名称
     */
    fun getModuleName(): String

    /**
     * 处理 JS 调用
     * @param functionName 要调用的方法名
     * @param params H5 传入的参数（JSON 字符串）
     * @param callback 处理完成后调用，传入code, msg, data三个参数
     */
    fun handle(functionName: String, params: String, callback: (code: String, msg: String, data: Any?) -> Unit)
}