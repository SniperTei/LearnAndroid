package com.sniper.webbox.container

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.sniper.webbox.R
import com.sniper.webbox.base.activity.BaseActivity
import com.sniper.webbox.web.activity.WebActivity

/**
 * 容器启动活动
 *
 * 这是应用的入口，负责：
 * 1. 加载容器配置
 * 2. 验证配置有效性
 * 3. 初始化容器实例
 * 4. 启动 WebActivity
 */
class ContainerActivity : BaseActivity() {

    private val CONTAINER_TAG = "ContainerActivity"

    private lateinit var progressBar: ProgressBar
    private lateinit var statusText: TextView

    override fun getLayoutId(): Int {
        return R.layout.activity_container
    }

    override fun initView() {
        super.initView()
        progressBar = findViewById(R.id.progress_bar)
        statusText = findViewById(R.id.status_text)
    }

    override fun initData() {
        super.initData()

        // 显示加载状态
        statusText.text = "正在加载容器配置..."

        // 在后台线程加载配置
        loadContainerConfig()
    }

    /**
     * 加载容器配置
     */
    private fun loadContainerConfig() {
        // 方法1: 从 assets 加载（默认）
        val loadSuccess = ContainerConfigManager.loadFromAssets(this)

        if (!loadSuccess) {
            showError("配置加载失败\n请确保 assets/container_config.json 存在")
            return
        }

        // 验证配置
        val (valid, error) = ContainerConfigManager.validateConfig()
        if (!valid) {
            showError("配置无效\n$error")
            return
        }

        // 打印配置摘要
        ContainerConfigManager.printConfigSummary()

        // 初始化容器
        initContainer()
    }

    /**
     * 初始化容器
     */
    private fun initContainer() {
        try {
            val config = ContainerConfigManager.getConfig()

            // 创建容器实例
            val container = WebContainer(this, config)
            container.initialize()

            statusText.text = "容器已就绪: ${config.appName}"

            // 延迟启动，让用户看到启动画面（可配置）
            progressBar.postDelayed({
                startWebActivity(config)
            }, 1000)

        } catch (e: Exception) {
            Log.e(CONTAINER_TAG, "❌ 容器初始化失败", e)
            showError("容器初始化失败\n${e.message}")
        }
    }

    /**
     * 启动 WebActivity
     */
    private fun startWebActivity(config: ContainerConfig) {
        val intent = Intent(this, WebActivity::class.java).apply {
            // 传递配置
            putExtra(WebActivity.EXTRA_CONFIG_JSON, config.toJson())
            // 可选：传递标题
            putExtra(WebActivity.EXTRA_TITLE, config.appName)
            // 可选：控制导航栏显示
            putExtra(WebActivity.EXTRA_SHOW_TOOLBAR, config.theme.showToolbar)
        }

        startActivity(intent)
        finish()
    }

    /**
     * 显示错误信息
     */
    private fun showError(message: String) {
        Log.e(CONTAINER_TAG, "❌ $message")
        statusText.text = message
        progressBar.visibility = View.GONE
        showLongToast(message)
    }

    override fun initListener() {
        // 无需监听器
    }
}
