package com.example.common_module.network.interceptor.logging

interface FormatPrinter {
    fun printJsonRequest(requestString: String?)
    fun printFileRequest(requestString: String?)
    fun printJsonResponse(time: Long, isSuccessful: Boolean, code: Int, headers: String?, response: String?, segments: List<String?>?, message: String?, responseUrl: String?)
    fun printFileResponse(time: Long, isSuccessful: Boolean, code: Int, headers: String?, segments: List<String?>?, message: String?, responseUrl: String?)
    fun printThrowable(throwable: Throwable?)
    fun printUploadProgress(progress: Long, total: Long)
    fun printDownloadProgress(progress: Long, total: Long)
}