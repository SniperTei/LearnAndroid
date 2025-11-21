package com.sniper.webbox.user.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sniper.webbox.R
import com.sniper.webbox.base.activity.BaseActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern

class RegisterActivity : BaseActivity() {

    // 表单字段
    private lateinit var emailPhoneInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var confirmPasswordInput: TextInputEditText
    private lateinit var nicknameInput: TextInputEditText
    private lateinit var birthdayInput: TextInputEditText
    private lateinit var genderGroup: android.widget.RadioGroup
    private lateinit var registerButton: com.google.android.material.button.MaterialButton
    
    // 表单布局
    private lateinit var emailPhoneLayout: TextInputLayout
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var confirmPasswordLayout: TextInputLayout
    
    // 日历实例用于日期选择
    private val calendar = Calendar.getInstance()
    
    // 验证手机号的正则表达式
    private val phonePattern = Pattern.compile("^1[3-9]\\d{9}$")

    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun initView() {
        // 初始化视图组件
        emailPhoneInput = findViewById(R.id.email_phone_input)
        passwordInput = findViewById(R.id.password_input)
        confirmPasswordInput = findViewById(R.id.confirm_password_input)
        nicknameInput = findViewById(R.id.nickname_input)
        birthdayInput = findViewById(R.id.birthday_input)
        genderGroup = findViewById(R.id.gender_group)
        registerButton = findViewById(R.id.register_button)
        
        // 初始化布局组件
        emailPhoneLayout = findViewById(R.id.email_phone_layout)
        passwordLayout = findViewById(R.id.password_layout)
        confirmPasswordLayout = findViewById(R.id.confirm_password_layout)
    }

    override fun initListener() {
        // 设置生日输入框点击事件，显示日期选择器
        birthdayInput.setOnClickListener {
            showDatePickerDialog()
        }
        
        // 设置注册按钮点击事件
        registerButton.setOnClickListener {
            registerUser()
        }
    }

    /**
     * 显示日期选择器对话框
     */
    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                // 更新日历
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                
                // 格式化日期并设置到输入框
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                birthdayInput.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        
        // 设置最大可选日期为当前日期
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    /**
     * 验证表单
     * @return 表单是否有效
     */
    private fun validateForm(): Boolean {
        var isValid = true
        
        // 重置所有错误状态
        emailPhoneLayout.error = null
        passwordLayout.error = null
        confirmPasswordLayout.error = null
        
        // 验证邮箱/手机号
        val emailPhone = emailPhoneInput.text.toString().trim()
        if (emailPhone.isEmpty()) {
            emailPhoneLayout.error = getString(R.string.input_valid_email_phone)
            isValid = false
        } else if (!isValidEmail(emailPhone) && !isValidPhone(emailPhone)) {
            emailPhoneLayout.error = getString(R.string.input_valid_email_phone)
            isValid = false
        }
        
        // 验证密码
        val password = passwordInput.text.toString()
        if (password.isEmpty() || password.length < 6) {
            passwordLayout.error = getString(R.string.password_too_short)
            isValid = false
        }
        
        // 验证确认密码
        val confirmPassword = confirmPasswordInput.text.toString()
        if (confirmPassword.isEmpty() || confirmPassword != password) {
            confirmPasswordLayout.error = getString(R.string.password_not_match)
            isValid = false
        }
        
        return isValid
    }

    /**
     * 验证邮箱格式
     */
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * 验证手机号格式
     */
    private fun isValidPhone(phone: String): Boolean {
        return phonePattern.matcher(phone).matches()
    }

    /**
     * 获取选中的性别
     */
    private fun getSelectedGender(): String {
        val selectedId = genderGroup.checkedRadioButtonId
        val radioButton = findViewById<RadioButton>(selectedId)
        return radioButton.text.toString()
    }

    /**
     * 处理用户注册
     */
    private fun registerUser() {
        // 验证表单
        if (!validateForm()) {
            return
        }
        
        // 显示加载指示器
        startLoading("注册中...")
        
        // 收集用户信息
        val emailPhone = emailPhoneInput.text.toString().trim()
        val password = passwordInput.text.toString()
        val nickname = nicknameInput.text.toString().trim()
        val gender = getSelectedGender()
        val birthday = birthdayInput.text.toString()
        
        // 模拟网络请求
        android.os.Handler().postDelayed({
            // 隐藏加载指示器
            stopLoading()
            
            // 注册成功，显示成功消息并返回登录页面
            showShortToast(getString(R.string.register_success))
            finish() // 关闭当前Activity，返回上一页
        }, 1500) // 模拟1.5秒的网络延迟
    }
}