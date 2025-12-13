package com.sniper.webbox.web.bridge

interface JSBridgeInterface {
    /**
     * H5 调用原生的统一入口：接收一个JSON字符串参数
     */
    fun callNative(data: String)

    /**
     * 原生主动调用 H5 的方法
     * @param callbackName H5 注册的回调函数名
     * @param data 要传递给 H5 的数据
     */
    fun callJs(callbackName: String, data: String)
}