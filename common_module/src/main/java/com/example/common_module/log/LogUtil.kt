package com.example.common_module.log

import android.util.Log

object LogUtil {
    private const val TAG = "LogUtil"
    private const val MAX_LENGTH = 4000

    fun debugInfo(tag: String?, msg: String?) {
        if (msg == null) return
        var msgTemp: String = msg
        var tagTemp = tag
        if (tagTemp.isNullOrEmpty()) {
            tagTemp = TAG
        }
        val length = msgTemp.length
        if (length <= MAX_LENGTH) {
            Log.d(tagTemp, msgTemp)
        } else {
            while (msgTemp.length > MAX_LENGTH) {
                val logContent = msgTemp.substring(0, MAX_LENGTH)
                msgTemp = msgTemp.replace(logContent, "")
                Log.d(tagTemp, logContent)
            }
            Log.d(tagTemp, msgTemp)
        }
    }

    fun warnInfo(tag: String?, msg: String?) {
        if (msg == null) return
        var msgTemp: String = msg
        var tagTemp = tag
        if (tagTemp.isNullOrEmpty()) {
            tagTemp = TAG
        }
        val length = msgTemp.length
        if (length <= MAX_LENGTH) {
            Log.w(tagTemp, msgTemp)
        } else {
            while (msgTemp.length > MAX_LENGTH) {
                val logContent = msgTemp.substring(0, MAX_LENGTH)
                msgTemp = msgTemp.replace(logContent, "")
                Log.w(tagTemp, logContent)
            }
            Log.w(tagTemp, msgTemp)
        }
    }
}