package com.example.my_module.service.impl

import android.app.Activity
import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.common_library.ext.lifecycle.AppActivityManger
import com.example.common_library.log.LogUtil
import com.example.core_library.service.login.LoginProvider
import com.example.my_module.R
import com.example.my_module.ui.fragment.LoginFragment

@Route(path = "/myModule/loginImpl", name = "我的相关服务")
class LoginImpl: LoginProvider {

    private val TAG = "LoginImpl"

    override fun init(context: Context?) {
        LogUtil.debugInfo(TAG, "LoginImpl init")
    }
    override fun login(username: String, password: String) {
        LogUtil.debugInfo(TAG, "login impl")
    }

    override fun register(username: String, password0: String, password1: String) {
        LogUtil.debugInfo(TAG, "register impl")
    }

    override fun logout() {
        LogUtil.debugInfo(TAG, "logout impl")
    }

    override fun showLoginPage() {
        LogUtil.debugInfo(TAG, "showLoginPage")
        val loginFragment = LoginFragment()
        // 获取fragmentManager
//        val fragmentManager = AppActivityManger.currentActivity
//        // 开启事务
//        val transaction = fragmentManager?.beginTransaction()
//        // 添加fragment
//        transaction?.add(R.id.fragment_container_view_tag, loginFragment)
////        // 提交事务
//        transaction?.commit()
    }
}