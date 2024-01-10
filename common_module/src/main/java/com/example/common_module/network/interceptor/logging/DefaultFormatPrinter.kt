package com.example.common_module.network.interceptor.logging

import com.example.common_module.util.LogUtil

class DefaultFormatPrinter: FormatPrinter {
    override fun printJsonRequest(requestString: String?) {
        LogUtil.debugInfo("printJsonRequest : ", requestString)
    }

    override fun printFileRequest(requestString: String?) {
        println("printFileRequest")
    }

    override fun printJsonResponse(
        time: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String?,
        response: String?,
        segments: List<String?>?,
        message: String?,
        responseUrl: String?
    ) {
        LogUtil.debugInfo("printJsonResponse : ", response)
    }

    override fun printFileResponse(
        time: Long,
        isSuccessful: Boolean,
        code: Int,
        headers: String?,
        segments: List<String?>?,
        message: String?,
        responseUrl: String?
    ) {
        println("printFileResponse")
    }

    override fun printThrowable(throwable: Throwable?) {
        println("printThrowable")
    }

    override fun printUploadProgress(progress: Long, total: Long) {
        println("printUploadProgress")
    }

    override fun printDownloadProgress(progress: Long, total: Long) {
        println("printDownloadProgress")
    }
}