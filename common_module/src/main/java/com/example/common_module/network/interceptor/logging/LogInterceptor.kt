package com.example.common_module.network.interceptor.logging

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import java.util.Locale

class LogInterceptor: Interceptor {

//    private val mPrinter: FormatPrinter = DefaultFormatPrinter()
    private val printLevel = Level.ALL

    constructor() {
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        // 
        val request = chain.request()
        val logRequest = printLevel == Level.ALL || printLevel == Level.REQUEST
        if (logRequest) {
        //    mPrinter.printJsonRequest(request, parseRequestBody(request))
            if (request.body != null && isParseable(request.body!!.contentType()!!)) {
                // mPrinter.printJsonRequest(request, parseRequestBody(request))
            } else {
                // mPrinter.printFileRequest(request)
            }
        }
        val logResponse = printLevel == Level.ALL || printLevel == Level.RESPONSE
        val t1 = if (logResponse) System.nanoTime() else 0
        val response = chain.proceed(request)
        val t2 = if (logResponse) System.nanoTime() else 0
        if (logResponse) {
            // val segmentList = request.url.encodedPathSegments
            // val header = response.headers.toString()
            // val code = response.code()
            // val isSuccessful = response.isSuccessful
            // val message = response.message()
            // val responseBody = response.body
            // val responseBodyString = responseBody?.string()
            // val requestUrl = response.request.url.toString()
            // val protocol = response.protocol.toString()
            // mPrinter.printJsonResponse(
            //     TimeUnit.NANOSECONDS.toMillis(t2 - t1), isSuccessful,
            //     code, header, responseBodyString, segmentList, message,
            //     protocol, requestUrl
            // )
            // response = response.newBuilder().body(ResponseBody.create(responseBody?.contentType(), responseBodyString)).build()
        }
        return response
    }
    /**
     * 是否可以解析
     */
    private fun isParseable(mediaType: MediaType?): Boolean {
        return isText(mediaType) || isPlain(mediaType) || isJson(mediaType) || isForm(mediaType) || isHtml(mediaType) || isXml(mediaType)
    }

    /**
     * 是否是纯文本
     */
    private fun isText(mediaType: MediaType?): Boolean {
        return mediaType?.type != null && mediaType.type == "text"
    }

    /**
     * 是否是JSON
     */
    private fun isJson(mediaType: MediaType?): Boolean {
        return if (mediaType?.subtype != null) {
            mediaType.subtype.lowercase(Locale.ROOT).contains("json")
        } else {
            false
        }
    }

    /**
     * 是否是纯文本
     */
    private fun isPlain(mediaType: MediaType?): Boolean {
        return if (mediaType?.subtype != null) {
            mediaType.subtype.lowercase(Locale.ROOT).contains("plain")
        } else {
            false
        }
    }

    /**
     * 是否是HTML
     */
    private fun isHtml(mediaType: MediaType?): Boolean {
        return if (mediaType?.subtype != null) {
            mediaType.subtype.lowercase(Locale.ROOT).contains("html")
        } else {
            false
        }
    }

    /**
     * 是否是xml
     */
    private fun isXml(mediaType: MediaType?): Boolean {
        return if (mediaType?.subtype != null) {
            mediaType.subtype.lowercase(Locale.ROOT).contains("xml")
        } else {
            false
        }
    }

    /**
     * 是否是表单
     */
    private fun isForm(mediaType: MediaType?): Boolean {
        return if (mediaType?.subtype != null) {
            mediaType.subtype.lowercase(Locale.ROOT).contains("x-www-form-urlencoded")
        } else {
            false
        }
    }

    enum class Level {
        /**
         * 不打印log
         */
        NONE,

        /**
         * 只打印请求信息
         */
        REQUEST,

        /**
         * 只打印响应信息
         */
        RESPONSE,

        /**
         * 所有数据全部打印
         */
        ALL
    }
}