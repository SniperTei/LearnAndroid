package com.sniper.webbox.user.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.sniper.webbox.R
import com.sniper.webbox.base.activity.BaseActivity

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
        
        // 这里添加实际的登录逻辑，例如调用API或本地验证
        Log.d(TAG, "Login attempt with username: $username")
        
        // 模拟登录成功
        showShortToast("登录成功")
        
        // 登录成功后可以跳转到主界面
        // startActivity(Intent(this, MainActivity::class.java))
        // finish()
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