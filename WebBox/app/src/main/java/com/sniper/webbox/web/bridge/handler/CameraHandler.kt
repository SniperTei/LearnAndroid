package com.sniper.webbox.web.bridge.handler

import android.content.Context
import android.content.Intent
import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.IOException
import android.util.Base64
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log

class CameraHandler(private val context: Context) : JSHandler {
    private val mainHandler = Handler(Looper.getMainLooper())
    private var currentCallback: ((String, String, Any?) -> Unit)? = null
    private val gson = Gson()

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
                // 拍照功能待实现
                callback("100001", "takePhoto not implemented", null)
            }
            "selectImage" -> {
                // 保存回调
                currentCallback = callback
                
                // 启动相册选择图片
                startImagePicker()
            }
            else -> {
                // 不支持的方法
                callback("100001", "function not supported", null)
            }
        }
    }

    /**
     * 启动相册选择图片
     */
    private fun startImagePicker() {
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent(Intent.ACTION_OPEN_DOCUMENT)
        } else {
            Intent(Intent.ACTION_GET_CONTENT)
        }
        
        // 设置为图片类型
        intent.type = "image/*"
        // 支持多选
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        // 允许读取文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        
        // 检查是否是Activity上下文
        if (context is AppCompatActivity) {
            // 注册ActivityResult回调
            val resultLauncher = context.registerForActivityResult(SelectMultipleImagesContract()) {
                handleImageSelectionResult(it)
            }
            resultLauncher.launch(Unit)
        } else {
            // 非Activity上下文无法启动Activity
            currentCallback?.invoke("900002", "Context is not an Activity", null)
            currentCallback = null
        }
    }

    /**
     * 处理图片选择结果
     */
    private fun handleImageSelectionResult(uris: List<Uri>?) {
        mainHandler.post {
            if (uris.isNullOrEmpty()) {
                currentCallback?.invoke("900003", "No images selected", null)
                currentCallback = null
                return@post
            }

            try {
                // 将选择的图片转换为Base64编码
                val base64List = uris.mapNotNull { uri ->
                    uriToBase64(uri)
                }

                // 返回结果给H5
                currentCallback?.invoke("000000", "success", base64List)
            } catch (e: Exception) {
                Log.e("CameraHandler", "Error processing images: ${e.message}", e)
                currentCallback?.invoke("900004", "Error processing images", null)
            } finally {
                currentCallback = null
            }
        }
    }

    /**
     * 将图片Uri转换为Base64编码
     * 优化内存使用，防止OOM
     */
    private fun uriToBase64(uri: Uri): String? {
        var inputStream: java.io.InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri)

            // 获取图片原始尺寸
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()

            // 计算采样率（限制最大尺寸为1024px）
            val maxDimension = 1024
            val sampleSize = calculateSampleSize(options.outWidth, options.outHeight, maxDimension)

            // 重新打开流进行解码
            inputStream = context.contentResolver.openInputStream(uri)
            val decodeOptions = BitmapFactory.Options().apply {
                inSampleSize = sampleSize
                inPreferredConfig = Bitmap.Config.RGB_565 // 减少内存占用
            }

            val bitmap = BitmapFactory.decodeStream(inputStream, null, decodeOptions)

            if (bitmap != null) {
                // 进一步压缩：如果还是太大，进行缩放
                val scaledBitmap = if (bitmap.width > maxDimension || bitmap.height > maxDimension) {
                    scaleBitmap(bitmap, maxDimension)
                } else {
                    bitmap
                }

                val outputStream = ByteArrayOutputStream()
                // 压缩图片质量，80%质量
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
                val bytes = outputStream.toByteArray()
                outputStream.close()

                // 回收Bitmap
                if (scaledBitmap != bitmap) {
                    bitmap.recycle()
                }
                scaledBitmap.recycle()

                // 限制最大文件大小为2MB
                val maxSize = 2 * 1024 * 1024 // 2MB
                return if (bytes.size > maxSize) {
                    // 如果还是太大，降低质量重新压缩
                    compressWithQuality(bytes, 60, maxSize)
                } else {
                    Base64.encodeToString(bytes, Base64.NO_WRAP)
                }
            } else {
                return null
            }
        } catch (e: OutOfMemoryError) {
            Log.e("CameraHandler", "OutOfMemoryError: Image too large", e)
            return null
        } catch (e: IOException) {
            Log.e("CameraHandler", "Error converting image to Base64: ${e.message}", e)
            return null
        } finally {
            try {
                inputStream?.close()
            } catch (e: Exception) {
                Log.e("CameraHandler", "Error closing input stream", e)
            }
        }
    }

    /**
     * 计算采样率
     * @param width 原始宽度
     * @param height 原始高度
     * @param maxDimension 最大尺寸
     * @return 采样率（必须是2的幂次方）
     */
    private fun calculateSampleSize(width: Int, height: Int, maxDimension: Int): Int {
        var inSampleSize = 1
        if (width > maxDimension || height > maxDimension) {
            val halfWidth = width / 2
            val halfHeight = height / 2

            while (halfWidth / inSampleSize >= maxDimension && halfHeight / inSampleSize >= maxDimension) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    /**
     * 缩放Bitmap
     * @param bitmap 原始Bitmap
     * @param maxDimension 最大尺寸
     * @return 缩放后的Bitmap
     */
    private fun scaleBitmap(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val scale = if (width > height) {
            maxDimension.toFloat() / width
        } else {
            maxDimension.toFloat() / height
        }

        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    /**
     * 使用指定质量压缩，直到满足大小限制
     * @param originalBytes 原始字节数组
     * @param quality 初始质量
     * @param maxSize 最大大小
     * @return Base64字符串
     */
    private fun compressWithQuality(originalBytes: ByteArray, quality: Int, maxSize: Int): String {
        val bitmap = BitmapFactory.decodeByteArray(originalBytes, 0, originalBytes.size)
        var currentQuality = quality
        var outputStream = ByteArrayOutputStream()

        while (currentQuality > 50) {
            outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, currentQuality, outputStream)
            val bytes = outputStream.toByteArray()

            if (bytes.size <= maxSize) {
                bitmap.recycle()
                return Base64.encodeToString(bytes, Base64.NO_WRAP)
            }

            currentQuality -= 10
        }

        bitmap.recycle()
        // 如果还是太大，返回最低质量的压缩结果
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }

    /**
     * 多选图片选择Contract
     */
    private class SelectMultipleImagesContract : ActivityResultContract<Unit, List<Uri>>() {
        override fun createIntent(context: Context, input: Unit): Intent {
            val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent(Intent.ACTION_OPEN_DOCUMENT)
            } else {
                Intent(Intent.ACTION_GET_CONTENT)
            }
            
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            
            return intent
        }

        override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
            val uris = mutableListOf<Uri>()
            
            if (resultCode == AppCompatActivity.RESULT_OK && intent != null) {
                // 处理单选情况
                val dataUri = intent.data
                if (dataUri != null) {
                    uris.add(dataUri)
                }
                
                // 处理多选情况
                val clipData = intent.clipData
                if (clipData != null) {
                    for (i in 0 until clipData.itemCount) {
                        val item = clipData.getItemAt(i)
                        uris.add(item.uri)
                    }
                }
            }
            
            return uris
        }
    }
}