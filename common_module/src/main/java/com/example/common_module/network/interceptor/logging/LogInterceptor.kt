package com.example.common_module.network.interceptor.logging

import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import okio.IOException
import java.nio.charset.Charset
import java.util.Locale
import java.util.concurrent.TimeUnit

class LogInterceptor: Interceptor {

    private val mPrinter: FormatPrinter = DefaultFormatPrinter()
    private val printLevel = Level.ALL

    constructor() {
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val logRequest =
            printLevel == Level.ALL || printLevel != Level.NONE && printLevel == Level.REQUEST
        if (logRequest) {
            //打印请求信息
            if (request.body != null && isParseable(
                    request.body!!.contentType()
                )
            ) {
//                mPrinter.printJsonRequest(request, parseParams(request))
            } else {
//                mPrinter.printFileRequest(request)
            }
        }
        val logResponse =
            printLevel == Level.ALL || printLevel != Level.NONE && printLevel == Level.RESPONSE
        val t1 = if (logResponse) System.nanoTime() else 0
        val originalResponse: Response
        originalResponse = try {
            chain.proceed(request)
        } catch (e: Exception) {
            e.message?.let {
                Log.d("Http Error: %s", it)
            }
            throw e
        }
        val t2 = if (logResponse) System.nanoTime() else 0
        val responseBody = originalResponse.body

        //打印响应结果
        var bodyString: String? = null
        if (responseBody != null && isParseable(responseBody.contentType())) {
//            bodyString = printResult(request, originalResponse, logResponse)
        }
        if (logResponse) {
            val segmentList =
                request.url.encodedPathSegments
            val header: String = if (originalResponse.networkResponse == null) {
                originalResponse.headers.toString()
            } else {
                originalResponse.networkResponse!!.request.headers.toString()
            }
            val code = originalResponse.code
            val isSuccessful = originalResponse.isSuccessful
            val message = originalResponse.message
            val url = originalResponse.request.url.toString()
            if (responseBody != null && isParseable(responseBody.contentType())) {
//                mPrinter.printJsonResponse(
//                    TimeUnit.NANOSECONDS.toMillis(t2 - t1), isSuccessful,
//                    code, header, responseBody.contentType, bodyString, segmentList, message, url
//                )
            } else {
                mPrinter.printFileResponse(
                    TimeUnit.NANOSECONDS.toMillis(t2 - t1),
                    isSuccessful, code, header, segmentList, message, url
                )
            }
        }
        return originalResponse
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

    fun parseRequestBody(request: Request): String {
        return try {
            val body = request.newBuilder().build().body ?: return ""
            val requestbuffer = Buffer()
            body.writeTo(requestbuffer)
            var charset = Charset.forName("UTF-8")
            val contentType = body.contentType()
            if (contentType != null) {
                charset = contentType.charset(charset)
            }
            var json = requestbuffer.readString(charset)
//            if (hasUrlEncoded(json!!)) {
//                json = URLDecoder.decode(
//                    json,
//                    convertCharset(charset)
//                )
//            }
            json
//            jsonFormat(json!!)
        } catch (e: IOException) {
            e.printStackTrace()
            "{\"error\": \"" + e.message + "\"}"
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