package com.sniper.webbox.web.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sniper.webbox.R
import com.sniper.webbox.base.activity.BaseActivity
import com.sniper.webbox.web.bridge.JSBridgeImpl
import com.sniper.webbox.web.bridge.JSBridgeManager
import com.sniper.webbox.web.bridge.handler.DeviceHandler

class WebActivity : BaseActivity() {
    companion object {
        const val EXTRA_URL = "extra_url"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_SHOW_TOOLBAR = "extra_show_toolbar"
        const val EXTRA_LOADING_MESSAGE = "extra_loading_message"
    }

    private lateinit var webView: WebView
    private lateinit var toolbarContainer: LinearLayout
    private lateinit var ivBack: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var ivClose: ImageView

    // JS Bridge实现实例
    private lateinit var jsBridgeImpl: JSBridgeImpl

    private var url: String? = null
    private var title: String? = null
    private var showToolbar = true
    private var loadingMessage = "加载中..."

    override fun getLayoutId(): Int {
        return R.layout.activity_web
    }

    override fun initView() {
        super.initView()
        webView = findViewById(R.id.web_view)
        toolbarContainer = findViewById(R.id.toolbar_container)
        ivBack = findViewById(R.id.iv_back)
        tvTitle = findViewById(R.id.tv_title)
        ivClose = findViewById(R.id.iv_close)

        // 初始化JS Bridge
        jsBridgeImpl = JSBridgeImpl(this, webView)

        // 配置WebView
        configureWebView()
    }

    override fun initData() {
        super.initData()
        // 获取传递的参数
        url = intent.getStringExtra(EXTRA_URL)
        title = intent.getStringExtra(EXTRA_TITLE)
        showToolbar = intent.getBooleanExtra(EXTRA_SHOW_TOOLBAR, true)
        loadingMessage = intent.getStringExtra(EXTRA_LOADING_MESSAGE) ?: "加载中..."

        // 设置标题
        title?.let { tvTitle.text = it }

        // 控制导航栏显示
        toolbarContainer.visibility = if (showToolbar) View.VISIBLE else View.GONE

        // 加载URL
        url?.let { loadUrl(it) }
    }

    override fun initListener() {
        super.initListener()
        // 返回按钮点击事件
        ivBack.setOnClickListener {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                finish()
            }
        }

        // 关闭按钮点击事件
        ivClose.setOnClickListener {
            finish()
        }
    }

    /**
     * 配置WebView的各种设置
     */
    private fun configureWebView() {
        val webSettings = webView.settings

        // 启用JavaScript
        webSettings.javaScriptEnabled = true

        // 允许混合内容（HTTP和HTTPS）
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        // 设置缓存模式
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT

        // 启用DOM存储
        webSettings.domStorageEnabled = true

        // 启用数据库存储
        webSettings.databaseEnabled = true

        // 启用App缓存
//        webSettings.setAppCacheEnabled(true)

        // 设置UserAgent
        webSettings.userAgentString = webSettings.userAgentString + " WebBoxApp"

        // 设置WebViewClient
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                return handleUrlLoading(url)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return handleUrlLoading(url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // 页面加载完成后隐藏加载指示器
                stopLoading()

                // 如果没有设置标题，尝试从网页获取标题
                if (title.isNullOrEmpty()) {
                    title = view?.title
                    tvTitle.text = title
                }
            }

            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                stopLoading()
                showShortToast("加载失败: $description")
            }
        }

        // 设置WebChromeClient
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                // 可以根据进度做一些UI更新
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                // 如果没有设置标题，使用网页标题
                if (this@WebActivity.title.isNullOrEmpty()) {
                    this@WebActivity.title = title
                    tvTitle.text = title
                }
            }
        }

        // 添加JavaScript接口
        webView.addJavascriptInterface(WebAppInterface(), "Android")
        
        // 注册JS处理器
        JSBridgeManager.instance.registerHandler(DeviceHandler(this))
    }

    /**
     * 处理URL加载
     */
    private fun handleUrlLoading(url: String?): Boolean {
        if (url.isNullOrEmpty()) return false

        // 处理常见的外部链接（如tel:, mailto:, market:等）
        when {
            url.startsWith("tel:") -> {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                startActivity(intent)
                return true
            }
            url.startsWith("mailto:") -> {
                val intent = Intent(Intent.ACTION_SENDTO, Uri.parse(url))
                startActivity(intent)
                return true
            }
            url.startsWith("market:") -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return true
            }
        }

        // 其他链接在WebView中加载
        return false
    }

    /**
     * 加载指定URL
     */
    fun loadUrl(url: String) {
        if (url.isNotEmpty()) {
            // 显示加载指示器
            startLoading(loadingMessage)
            webView.loadUrl(url)
        } else {
            showShortToast("URL不能为空")
        }
    }

    /**
     * JavaScript接口类，供网页调用Android方法
     */
    inner class WebAppInterface {
        @JavascriptInterface
        fun showToast(message: String) {
            runOnUiThread { 
                showShortToast(message)
            }
        }

        @JavascriptInterface
        fun finishActivity() {
            runOnUiThread {
                finish()
            }
        }
        
        @JavascriptInterface
        fun callNative(data: String) {
            // 解析JS传来的调用数据
            try {
                val jsonObject = org.json.JSONObject(data)
                val method = jsonObject.optString("method", "")
                val params = jsonObject.optString("params", "")
                val callbackId = jsonObject.optString("callbackId", "")
                
                // 调用JSBridgeImpl处理原生调用
                jsBridgeImpl.callNative(method, params, callbackId)
            } catch (e: Exception) {
                // 解析失败，通过错误回调通知JS
                val errorCallback = "onNativeCallback('${System.currentTimeMillis()}', 'error', '{\"code\":\"900001\",\"msg\":\"解析调用数据失败: ${e.message}\",\"data\":null}')"
                webView.evaluateJavascript(errorCallback, null)
            }
        }
    }

    /**
     * 处理返回键事件
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     * 处理应用进入后台
     */
    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    /**
     * 处理应用恢复前台
     */
    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    /**
     * 处理Activity销毁
     */
    override fun onDestroy() {
        // 清理WebView资源
        webView.removeAllViews()
        webView.destroy()
        super.onDestroy()
    }
}