package com.sniper.webbox.network.manager

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sniper.webbox.BuildConfig
import com.sniper.webbox.network.api.ApiRequest
import com.sniper.webbox.network.config.NetworkConfig
import com.sniper.webbox.network.enums.ParamType
import com.sniper.webbox.network.enums.RequestMethod
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 网络请求管理类
 * 封装OkHttp的网络请求操作
 */
object NetworkManager {
    private val gson: Gson = GsonBuilder()
        .serializeNulls()
        .setLenient()
        .create()
    private var okHttpClient: OkHttpClient? = null
    private var cacheDir: File? = null

    /**
     * 设置缓存目录
     * @param dir 缓存目录
     */
    fun setCacheDir(dir: File) {
        this.cacheDir = dir
    }

    /**
     * 获取OkHttpClient实例
     * @return OkHttpClient实例
     */
    private fun getOkHttpClient(): OkHttpClient {
        if (okHttpClient == null) {
            okHttpClient = buildOkHttpClient()
        }
        return okHttpClient!!
    }

    /**
     * 构建OkHttpClient实例
     * @return OkHttpClient实例
     */
    private fun buildOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        // 添加日志拦截器
        if (NetworkConfig.logEnable) {
            val loggingInterceptor = HttpLoggingInterceptor()
            // 在生产环境，只记录基本信息
            loggingInterceptor.level = if (isProductionBuild()) {
                HttpLoggingInterceptor.Level.BASIC
            } else {
                HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(loggingInterceptor)
        }

        // 添加认证拦截器
        builder.addInterceptor(AuthInterceptor())

        // 添加默认请求头拦截器
        builder.addInterceptor(DefaultHeaderInterceptor())

        // 配置超时时间
        builder.connectTimeout(NetworkConfig.connectTimeout, TimeUnit.MILLISECONDS)
        builder.readTimeout(NetworkConfig.readTimeout, TimeUnit.MILLISECONDS)
        builder.writeTimeout(NetworkConfig.writeTimeout, TimeUnit.MILLISECONDS)

        // 配置连接池
        builder.connectionPool(ConnectionPool(10, 30, TimeUnit.SECONDS))

        // 配置缓存
        if (NetworkConfig.cacheEnable && cacheDir != null) {
            builder.cache(Cache(cacheDir!!, NetworkConfig.cacheSize))
        }

        // ⚠️ SSL证书验证配置
        // 仅在开发环境且明确配置时才忽略SSL验证
        // 生产环境必须启用SSL验证
        if (!isProductionBuild() && NetworkConfig.ignoreSSLVerify) {
            configureSSLSkipVerify(builder)
        }

        return builder.build()
    }

    /**
     * 判断是否为生产构建
     * @return 是否为生产环境
     */
    private fun isProductionBuild(): Boolean {
        // 通过BuildConfig.DEBUG判断，或通过检查域名
        val baseUrl = NetworkConfig.getBaseUrl()
        return !BuildConfig.DEBUG &&
                !baseUrl.contains("localhost") &&
                !baseUrl.contains("10.0.2.2") &&
                !baseUrl.contains("192.168")
    }

    /**
     * 配置忽略SSL证书验证
     * ⚠️ 仅用于开发环境，生产环境切勿使用
     */
    private fun configureSSLSkipVerify(builder: OkHttpClient.Builder) {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

                override fun getAcceptedIssuers(): Array<X509Certificate?> { return arrayOfNulls(0) }
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            builder.sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { _, _ -> true }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 执行网络请求（协程版本）
     * @param request ApiRequest接口实现
     * @return 网络响应字符串
     */
    suspend fun execute(request: ApiRequest): String {
        return suspendCoroutine { continuation ->
            val httpRequest = buildRequest(request)
            val call = getOkHttpClient().newCall(httpRequest)
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (response.isSuccessful) {
                            val body = response.body?.string() ?: ""
                            continuation.resume(body)
                        } else {
                            continuation.resumeWithException(IOException("Request failed with code: ${response.code}"))
                        }
                    } catch (e: Exception) {
                        continuation.resumeWithException(e)
                    } finally {
                        response.close()
                    }
                }
            })
        }
    }

    /**
     * 执行网络请求并解析为指定类型（协程版本）
     * @param request ApiRequest接口实现
     * @return 解析后的对象
     */
    suspend fun <T> executeAndParse(request: ApiRequest, clazz: Class<T>): T {
        val responseString = execute(request)
        return gson.fromJson(responseString, clazz)
    }

    /**
     * 构建Request对象
     * @param request ApiRequest接口实现
     * @return Request对象
     */
    private fun buildRequest(request: ApiRequest): Request {
        val url = buildUrl(request.getApiPath())
        val requestBuilder = Request.Builder().url(url)

        // 添加请求头
        addHeaders(requestBuilder, request)

        // 根据请求方法和参数类型构建请求体
        when (request.getRequestMethod()) {
            RequestMethod.GET -> {
                // GET请求的参数已经在URL中添加
            }
            RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE -> {
                val body = buildRequestBody(request)
                requestBuilder.method(request.getRequestMethod().name, body)
            }
            RequestMethod.UPLOAD -> {
                // 上传文件的处理
                val body = buildMultipartBody(request)
                requestBuilder.method(request.getRequestMethod().name, body)
            }
            RequestMethod.DOWNLOAD -> {
                // 下载文件的处理
                requestBuilder.method(request.getRequestMethod().name, null)
            }
        }

        return requestBuilder.build()
    }

    /**
     * 构建完整的URL
     * @param apiPath 接口路径
     * @return 完整的URL
     */
    private fun buildUrl(apiPath: String): HttpUrl {
        val baseUrl = NetworkConfig.getBaseUrl().trimEnd('/')
        val path = apiPath.trimStart('/')
        val urlBuilder = "$baseUrl/$path".toHttpUrlOrNull()?.newBuilder()
            ?: throw IllegalArgumentException("Invalid URL: $baseUrl/$path")
        return urlBuilder.build()
    }

    /**
     * 添加请求头
     * @param requestBuilder Request.Builder
     * @param request ApiRequest接口实现
     */
    private fun addHeaders(requestBuilder: Request.Builder, request: ApiRequest) {
        // 添加默认请求头
        requestBuilder.addHeader("Content-Type", "application/json")
        requestBuilder.addHeader("Accept", "application/json")
        requestBuilder.addHeader("User-Agent", NetworkConfig.userAgent)

        // 添加认证头
        if (request.needAuth()) {
            val fullToken = com.sniper.webbox.user.manager.AppUserManager.getFullAuthToken()
            if (!fullToken.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", fullToken)
            }
        }

        // 添加自定义请求头
        request.getHeaders().forEach { (key, value) ->
            requestBuilder.addHeader(key, value)
        }
    }

    /**
     * 构建请求体
     * @param request ApiRequest接口实现
     * @return RequestBody对象
     */
    private fun buildRequestBody(request: ApiRequest): RequestBody? {
        val params = request.getParams() ?: return null

        return when (request.getParamType()) {
            ParamType.JSON_STRING -> {
                val jsonString = params as String
                RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), jsonString)
            }
            ParamType.MAP -> {
                val map = params as Map<String, Any>
                val jsonString = gson.toJson(map)
                RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), jsonString)
            }
            ParamType.FORM_BODY -> {
                val map = params as Map<String, String>
                val formBodyBuilder = FormBody.Builder()
                map.forEach { (key, value) ->
                    formBodyBuilder.add(key, value)
                }
                formBodyBuilder.build()
            }
            ParamType.NONE -> null
        }
    }

    /**
     * 构建多部分请求体（用于文件上传）
     * @param request ApiRequest接口实现
     * @return MultipartBody对象
     */
    private fun buildMultipartBody(request: ApiRequest): RequestBody {
        val params = request.getParams() as? Map<String, Any> ?: emptyMap()
        val builder = MultipartBody.Builder()
            .setType(MultipartBody.FORM)

        params.forEach { (key, value) ->
            when (value) {
                is File -> {
                    val fileBody = RequestBody.create("application/octet-stream".toMediaTypeOrNull(), value)
                    builder.addFormDataPart(key, value.name, fileBody)
                }
                else -> {
                    builder.addFormDataPart(key, value.toString())
                }
            }
        }

        return builder.build()
    }

    /**
     * 认证拦截器
     * 用于添加认证相关的请求头
     */
    private class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()

            // 从AppUserManager获取认证信息
            val fullToken = com.sniper.webbox.user.manager.AppUserManager.getFullAuthToken()
            if (!fullToken.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", fullToken)
            }

            return chain.proceed(requestBuilder.build())
        }
    }

    /**
     * 默认请求头拦截器
     * 用于添加默认的请求头
     */
    private class DefaultHeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", NetworkConfig.userAgent)

            return chain.proceed(requestBuilder.build())
        }
    }

    /**
     * 清除缓存的OkHttpClient实例
     * 当配置改变时需要调用此方法
     */
    fun clearCache() {
        okHttpClient = null
    }
}