package com.sniper.webbox.web.bridge.handler

import android.app.AlertDialog
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
import androidx.core.content.FileProvider
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
import android.widget.ArrayAdapter

class CameraHandler(private val context: Context) : JSHandler {
    private val mainHandler = Handler(Looper.getMainLooper())
    private var currentCallback: ((String, String, Any?) -> Unit)? = null
    private val gson = Gson()

    // 拍照相关
    private var cameraPermissionLauncher: androidx.activity.result.ActivityResultLauncher<String>? = null
    private var takePhotoLauncher: androidx.activity.result.ActivityResultLauncher<Uri>? = null
    private var photoUri: Uri? = null

    // 图片选择相关
    private var selectImageLauncher: androidx.activity.result.ActivityResultLauncher<Unit>? = null

    init {
        // 在初始化时注册所有 ActivityResultLauncher
        if (context is AppCompatActivity) {
            // 注册图片选择 launcher
            selectImageLauncher = context.registerForActivityResult(SelectMultipleImagesContract()) { uris ->
                handleImageSelectionResult(uris)
            }

            // 注册相机权限 launcher
            cameraPermissionLauncher = context.registerForActivityResult(
                androidx.activity.result.contract.ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                mainHandler.post {
                    if (isGranted) {
                        launchCamera(context)
                    } else {
                        currentCallback?.invoke("900001", "Camera permission denied", null)
                        currentCallback = null
                    }
                }
            }

            // 注册拍照 launcher
            takePhotoLauncher = context.registerForActivityResult(
                androidx.activity.result.contract.ActivityResultContracts.TakePicture()
            ) { success ->
                handlePhotoResult(success)
            }
        }
    }

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
                Log.d("CameraHandler", "takePhoto called")
                // 保存回调
                currentCallback = callback

                // 检查并请求相机权限
                checkAndRequestCameraPermission()
            }
            "selectImage" -> {
                // 保存回调
                currentCallback = callback

                // 启动相册选择图片
                startImagePicker()
            }
            "showImagePickerDialog" -> {
                Log.d("CameraHandler", "showImagePickerDialog called")
                // 保存回调
                currentCallback = callback

                // 显示选择对话框
                showImageSourceDialog()
            }
            else -> {
                // 不支持的方法
                callback("100001", "function not supported", null)
            }
        }
    }

    /**
     * 显示图片来源选择对话框
     * 让用户选择是拍照还是从相册选择
     */
    private fun showImageSourceDialog() {
        mainHandler.post {
            val options = arrayOf("拍照", "从相册选择")

            val builder = AlertDialog.Builder(context)
            builder.setTitle("选择图片来源")
                .setItems(options) { dialog, which ->
                    when (which) {
                        0 -> {
                            // 选择拍照
                            Log.d("CameraHandler", "用户选择拍照")
                            checkAndRequestCameraPermission()
                        }
                        1 -> {
                            // 选择相册
                            Log.d("CameraHandler", "用户选择相册")
                            startImagePicker()
                        }
                    }
                    dialog.dismiss()
                }
                .setNegativeButton("取消") { dialog, _ ->
                    // 用户取消
                    currentCallback?.invoke("900401", "用户取消", null)
                    currentCallback = null
                    dialog.dismiss()
                }

            val dialog = builder.create()
            dialog.show()
        }
    }

    /**
     * 启动相册选择图片
     */
    private fun startImagePicker() {
        // 检查 launcher 是否已注册
        if (selectImageLauncher == null) {
            mainHandler.post {
                currentCallback?.invoke("900002", "selectImageLauncher not initialized", null)
                currentCallback = null
            }
            return
        }

        // 启动相册选择器（Intent 已在 Contract 中定义）
        selectImageLauncher?.launch(Unit)
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
            // 使用 ACTION_PICK 打开相册
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
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

    /**
     * 检查并请求相机权限
     */
    private fun checkAndRequestCameraPermission() {
        val activity = context as? AppCompatActivity
        if (activity == null) {
            mainHandler.post {
                currentCallback?.invoke("900002", "Context is not an Activity", null)
                currentCallback = null
            }
            return
        }

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                if (activity.checkSelfPermission(android.Manifest.permission.CAMERA)
                    == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    launchCamera(activity)
                } else {
                    requestCameraPermission(activity)
                }
            }
            else -> {
                launchCamera(activity)
            }
        }
    }

    /**
     * 请求相机权限
     */
    private fun requestCameraPermission(activity: AppCompatActivity) {
        // Launcher 已在 init 中注册，直接调用
        cameraPermissionLauncher?.launch(android.Manifest.permission.CAMERA) ?: run {
            mainHandler.post {
                currentCallback?.invoke("900002", "Camera permission launcher not initialized", null)
                currentCallback = null
            }
        }
    }

    /**
     * 启动相机拍照
     */
    private fun launchCamera(activity: AppCompatActivity) {
        try {
            val photoFile = createImageFile()
            photoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    photoFile
                )
            } else {
                Uri.fromFile(photoFile)
            }

            // Launcher 已在 init 中注册，直接调用
            // 确保 photoUri 不为 null
            photoUri?.let { uri ->
                takePhotoLauncher?.launch(uri)
            } ?: run {
                throw IllegalStateException("Failed to create photo URI")
            }

        } catch (e: Exception) {
            Log.e("CameraHandler", "Error launching camera: ${e.message}", e)
            mainHandler.post {
                currentCallback?.invoke("900005", "Failed to launch camera: ${e.message}", null)
                currentCallback = null
            }
        }
    }

    /**
     * 处理拍照结果
     */
    private fun handlePhotoResult(success: Boolean) {
        mainHandler.post {
            if (!success) {
                currentCallback?.invoke("900006", "User cancelled taking photo", null)
                currentCallback = null
                return@post
            }

            try {
                val uri = photoUri
                if (uri != null) {
                    val base64 = uriToBase64(uri)
                    if (base64 != null) {
                        currentCallback?.invoke("000000", "success", base64)
                    } else {
                        currentCallback?.invoke("900007", "Failed to process photo", null)
                    }
                } else {
                    currentCallback?.invoke("900008", "Photo URI is null", null)
                }
            } catch (e: Exception) {
                Log.e("CameraHandler", "Error handling photo result: ${e.message}", e)
                currentCallback?.invoke("900009", "Error handling photo result: ${e.message}", null)
            } finally {
                currentCallback = null
                photoUri = null
            }
        }
    }

    /**
     * 创建临时图片文件
     */
    @Throws(IOException::class)
    private fun createImageFile(): java.io.File {
        val timeStamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault())
            .format(java.util.Date())

        val storageDir: java.io.File = context.externalCacheDir ?: context.cacheDir

        return java.io.File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
}