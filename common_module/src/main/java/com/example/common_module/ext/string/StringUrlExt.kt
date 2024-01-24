package com.example.common_module.ext.string

/**
 * 判断 str 是否已经 URLEncoder.encode() 过
 * 经常遇到这样的情况, 拿到一个 URL, 但是搞不清楚到底要不要 URLEncoder.encode()
 * 不做 URLEncoder.encode() 吧, 担心出错, 做 URLEncoder.encode() 吧, 又怕重复了
 *
 * @param str 需要判断的内容
 * @return 返回 `true` 为被 URLEncoder.encode() 过
 */
fun String.hasUrlEncoded(): Boolean {
    var encode = false
    /**
     * 判断 c 是否是 16 进制的字符
     *
     * @param c 需要判断的字符
     * @return 返回 `true` 为 16 进制的字符
     */
    fun isValidHexChar(c: Char): Boolean {
        return c in '0'..'9' || c in 'a'..'f' || c in 'A'..'F'
    }

    for (i in this.indices) {
        val c = this[i]
        if (c == '%' && i + 2 < this.length) {
            // 判断是否符合urlEncode规范
            val c1 = this[i + 1]
            val c2 = this[i + 2]
            if (isValidHexChar(c1) && isValidHexChar(c2)) {
                encode = true
                break
            } else {
                break
            }
        }
    }
    return encode

}