package com.sniper.webbox.base.activity

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * 基础Activity类，封装通用功能和行为
 * 所有Activity应继承此类以获得统一的功能支持
 */
abstract class BaseActivity : AppCompatActivity() {
    protected val TAG = this::class.java.simpleName
    
    // 加载指示器
    private var progressBar: ProgressBar? = null
    // 加载提示文本
    private var loadingText: TextView? = null
    // 加载视图容器
    private var loadingContainer: FrameLayout? = null
    // 当前加载状态
    private var isLoading = false
    
    // 根布局ID，子类必须实现此方法返回布局资源ID
    protected abstract fun getLayoutId(): Int
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置边缘到边缘显示
        enableEdgeToEdge()
        // 加载布局
        setContentView(getLayoutId())
        // 处理系统UI边缘
        handleSystemUI()
        // 执行初始化流程
        initView()
        initData()
        initListener()
        Log.d(TAG, "onCreate: Activity created")
    }
    
    /**
     * 显示加载指示器
     * @param message 加载提示文本，默认为"加载中..."
     */
    protected fun startLoading(message: String = "加载中...") {
        if (isLoading) return
        
        // 创建加载视图
        if (loadingContainer == null) {
            createLoadingView()
        }
        
        // 更新加载文本
        loadingText?.text = message
        
        // 显示加载视图
        loadingContainer?.visibility = View.VISIBLE
        isLoading = true
        
        // 禁用背景点击
        loadingContainer?.isClickable = true
    }
    
    /**
     * 隐藏加载指示器
     */
    protected fun stopLoading() {
        if (!isLoading) return
        
        loadingContainer?.visibility = View.GONE
        isLoading = false
        
        // 恢复背景点击
        loadingContainer?.isClickable = false
    }
    
    /**
     * 创建加载视图
     */
    private fun createLoadingView() {
        val contentView = findViewById<ViewGroup>(android.R.id.content)
        
        // 创建加载容器
        loadingContainer = FrameLayout(this).apply {
            // 设置布局参数
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            // 设置背景为半透明
            setBackgroundColor(android.graphics.Color.parseColor("#80000000"))
            // 使用setForegroundGravity替代gravity
            setForegroundGravity(Gravity.CENTER)
            // 禁用点击穿透
            isClickable = true
            isFocusable = true
        }
        
        // 创建加载内容容器
        val loadingContent = LinearLayout(this).apply {
            // 设置垂直方向
            orientation = LinearLayout.VERTICAL
            // 设置白色背景
            setBackgroundColor(android.graphics.Color.WHITE)
            // 使用硬编码的像素值计算边距（约等于16dp）
            val padding = (16 * resources.displayMetrics.density).toInt()
            setPadding(padding, padding, padding, padding)
            
            // 使用setHorizontalGravity和setVerticalGravity替代gravity
            setHorizontalGravity(Gravity.CENTER)
            setVerticalGravity(Gravity.CENTER)
        }
        
        // 设置布局参数
        val contentLayoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        contentLayoutParams.gravity = Gravity.CENTER
        loadingContent.layoutParams = contentLayoutParams
        
        // 创建ProgressBar
        progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleLarge).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }
        
        // 创建加载文本
        loadingText = TextView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = "加载中..."
            setTextColor(android.graphics.Color.BLACK)
            textSize = 16f
            // 重新计算padding以避免作用域问题
            val textPadding = (16 * resources.displayMetrics.density).toInt()
            setPadding(0, textPadding, 0, 0)
        }
        
        // 组装加载视图
        loadingContent.addView(progressBar)
        loadingContent.addView(loadingText)
        loadingContainer?.addView(loadingContent)
        
        // 添加到内容视图
        contentView.addView(loadingContainer)
    }
    
    /**
     * 处理系统UI边缘，确保内容不会被刘海、状态栏或导航栏遮挡
     */
    private fun handleSystemUI() {
        try {
            // 获取内容视图容器
            val contentView = findViewById<ViewGroup>(android.R.id.content)
            // 确保contentView不为null且有子视图
            if (contentView != null && contentView.childCount > 0) {
                val rootView = contentView.getChildAt(0)
                ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                    insets
                }
            } else {
                Log.w(TAG, "Content view is either null or has no children")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error handling system UI: ${e.message}")
        }
    }
    
    /**
     * 初始化视图组件
     * 子类可重写此方法进行视图相关初始化
     */
    protected open fun initView() {}
    
    /**
     * 初始化数据
     * 子类可重写此方法进行数据加载和处理
     */
    protected open fun initData() {}
    
    /**
     * 初始化事件监听器
     * 子类可重写此方法设置各种点击、触摸等事件监听
     */
    protected open fun initListener() {}
    
    /**
     * 显示短时间Toast提示
     * @param message 提示信息
     */
    protected fun showShortToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    /**
     * 显示长时间Toast提示
     * @param message 提示信息
     */
    protected fun showLongToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
    
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Activity started")
    }
    
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Activity resumed")
    }
    
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Activity paused")
    }
    
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Activity stopped")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Activity destroyed")
    }
    
    /**
     * 显示加载状态（兼容旧方法）
     * 子类可重写此方法自定义加载指示器的显示逻辑
     */
    protected open fun showLoading() {
        startLoading()
    }
    
    /**
     * 隐藏加载状态（兼容旧方法）
     * 子类可重写此方法自定义加载指示器的隐藏逻辑
     */
    protected open fun hideLoading() {
        stopLoading()
    }
}