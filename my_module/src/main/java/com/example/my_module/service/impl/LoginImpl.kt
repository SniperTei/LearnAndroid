package com.example.my_module.service.impl

import android.content.Context
import com.example.common_library.ext.lifecycle.AppActivityManger
import com.example.common_library.log.LogUtil
import com.example.core_library.service.login.LoginProvider
import com.example.my_module.R
import com.example.my_module.ui.fragment.LoginFragment

class LoginImpl: LoginProvider {

    private val TAG = "LoginImpl"

    override fun init(context: Context?) {
        LogUtil.debugInfo(TAG, "LoginImpl init")
    }
    override fun login(username: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun register(username: String, password0: String, password1: String) {
        TODO("Not yet implemented")
    }

    override fun logout() {
        TODO("Not yet implemented")
    }

    override fun showLoginPage() {
        val loginFragment = LoginFragment()
        // 获取fragmentManager
        val fragmentManager = AppActivityManger.currentActivity?.supportFragmentManager
        // 开启事务
        val transaction = fragmentManager?.beginTransaction()
        // 添加fragment
//        transaction?.add(R.id.fragment_container, loginFragment)
//        // 提交事务
//        transaction?.commit()
    }
}