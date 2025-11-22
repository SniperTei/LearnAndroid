package com.sniper.webbox.user.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputLayout
import com.sniper.webbox.R
import com.sniper.webbox.base.activity.BaseActivity
import com.sniper.webbox.network.api.UserApi
import com.sniper.webbox.user.manager.AppUserManager
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {
    
    // UI元素引用
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView
    private lateinit var forgotPasswordLink: TextView
    private lateinit var wechatLoginButton: ImageButton
    private lateinit var qqLoginButton: ImageButton

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        // 初始化UI元素
        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)
        registerLink = findViewById(R.id.register_link)
        forgotPasswordLink = findViewById(R.id.forgot_password)
        wechatLoginButton = findViewById(R.id.wechat_login)
        qqLoginButton = findViewById(R.id.qq_login)
    }

    override fun initListener() {
        // 登录按钮点击事件
        loginButton.setOnClickListener {
            performLogin()
        }
        
        // 注册链接点击事件
        registerLink.setOnClickListener {
            navigateToRegister()
        }
        
        // 忘记密码链接点击事件
        forgotPasswordLink.setOnClickListener {
            handleForgotPassword()
        }
        
        // 微信登录按钮点击事件
        wechatLoginButton.setOnClickListener {
            handleWechatLogin()
        }
        
        // QQ登录按钮点击事件
        qqLoginButton.setOnClickListener {
            handleQQLogin()
        }
    }
    
    private fun performLogin() {
        // 获取用户输入
        val username = usernameInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()
        
        // 简单的输入验证
        if (TextUtils.isEmpty(username)) {
            showShortToast("请输入用户名")
            return
        }
        
        if (TextUtils.isEmpty(password)) {
            showShortToast("请输入密码")
            return
        }
        
        // 显示加载状态
        startLoading("登录中...")
        
        // 这里添加实际的登录逻辑，例如调用API或本地验证
        Log.d(TAG, "Login attempt with username: $username")
        
        // 使用协程执行登录请求
        lifecycleScope.launch {
            try {
                // 调用登录API
                val response = UserApi().login(username, password)
                
                // 停止加载状态
                stopLoading()
                
                if (response.isSuccess()) {
                    // 登录成功，保存认证信息
                    response.data?.let { loginData ->
                        AppUserManager.saveAuthInfo(loginData.access_token, loginData.token_type)
                        Log.d(TAG, "Login successful, token saved")
                        
                        // 登录成功后，获取用户详细信息
                        fetchUserInfo()
                    }
                } else {
                    // 登录失败，显示错误信息
                    showShortToast(response.msg.ifEmpty { "登录失败，请检查用户名和密码" })
                    Log.e(TAG, "Login failed: ${response.msg}")
                }
            } catch (e: Exception) {
                // 隐藏加载状态
                hideLoading()
                
                // 处理网络异常
                showShortToast("登录失败: ${e.message}")
                Log.e(TAG, "Login error: ${e.message}", e)
            }
        }
    }
    
    /**
     * 获取用户详细信息
     */
    private fun fetchUserInfo() {
        lifecycleScope.launch {
            try {
                val response = UserApi().getUserInfo()
                
                if (response.isSuccess()) {
                    // 保存用户信息
                    response.data?.let { userInfo ->
                        AppUserManager.saveUserInfo(userInfo)
                        Log.d(TAG, "User info fetched and saved: ${userInfo.username}")
                        
                        // 登录和获取用户信息都成功后，跳转到主界面
                        showShortToast("登录成功")
                        // 这里需要替换为实际的主界面Activity
                        // startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    // 虽然登录成功，但获取用户信息失败，也视为登录成功
                    // 可以后续再获取用户信息
                    showShortToast("登录成功")
                    // startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            } catch (e: Exception) {
                // 获取用户信息失败，但登录已经成功，可以后续再获取
                Log.w(TAG, "Failed to fetch user info: ${e.message}", e)
                showShortToast("登录成功")
                // startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
        }
    }
    
    private fun navigateToRegister() {
        // 跳转到注册页面
        // 检查RegisterActivity是否存在，如果不存在可以先注释掉
        try {
            val intent = Intent(this, Class.forName("com.sniper.webbox.user.activity.RegisterActivity"))
            startActivity(intent)
        } catch (e: ClassNotFoundException) {
            Log.e(TAG, "RegisterActivity not found", e)
            showShortToast("注册功能暂未实现")
        }
    }
    
    private fun handleForgotPassword() {
        // 处理忘记密码逻辑
        showShortToast("忘记密码功能暂未实现")
    }
    
    private fun handleWechatLogin() {
        // 处理微信登录逻辑
        showShortToast("微信登录功能暂未实现")
    }
    
    private fun handleQQLogin() {
        // 处理QQ登录逻辑
        showShortToast("QQ登录功能暂未实现")
    }
}