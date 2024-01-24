package com.example.common_module.ext.string

import android.text.TextUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.StringReader
import java.io.StringWriter
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

/**
 * json 格式化
 *
 * @return
 */
fun String.jsonFormat(): String {
    var strJSON = this
    if (TextUtils.isEmpty(strJSON)) {
        return "Empty/Null json content"
    }
    var message: String
    try {
        strJSON = strJSON.trim { it <= ' ' }
        message = if (strJSON.startsWith("{")) {
            val jsonObject = JSONObject(strJSON)
            jsonObject.toString(4)
        } else if (strJSON.startsWith("[")) {
            val jsonArray = JSONArray(strJSON)
            jsonArray.toString(4)
        } else {
            strJSON
        }
    } catch (e: JSONException) {
        message = strJSON
    } catch (error: OutOfMemoryError) {
        message = "Output omitted because of Object size"
    }
    return message
}

/**
 * xml 格式化
 *
 * @return
 */
fun String.xmlFormat(): String? {
    if (TextUtils.isEmpty(this)) {
        return "Empty/Null xml content"
    }
    val message: String? = try {
        val xmlInput: Source =
            StreamSource(StringReader(this))
        val xmlOutput =
            StreamResult(StringWriter())
        val transformer =
            TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        transformer.transform(xmlInput, xmlOutput)
        xmlOutput.writer.toString().replaceFirst(">".toRegex(), ">\n")
    } catch (e: TransformerException) {
        this
    }
    return message
}