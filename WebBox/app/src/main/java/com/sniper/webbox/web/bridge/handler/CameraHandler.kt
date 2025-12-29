package com.sniper.webbox.web.bridge.handler

class CameraHandler: JSHandler {
    override fun getModuleName(): String {
        return "camera"
    }

    override fun handle(
        functionName: String,
        params: String,
        callback: (String, String, Any?) -> Unit
    ) {
        when (functionName) {
            "takePhoto" -> {
                // 拍照
            }
            "selectImage" -> {
                // 选择图片
            }
            else -> {
                // 不支持的方法
                callback("100001", "function not supported", null)
            }
        }
    }
}