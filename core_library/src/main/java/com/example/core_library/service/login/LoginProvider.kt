package com.example.core_library.service.login

import androidx.fragment.app.FragmentManager
import com.alibaba.android.arouter.facade.template.IProvider
import org.json.JSONObject

interface LoginProvider: IProvider {
    fun login(username: String, password: String)

    fun register(username: String, password0: String, password1: String)

    fun logout()

    fun showLoginPage(supportFragmentManager: FragmentManager)

    fun showRegisterPage(supportFragmentManager: FragmentManager)
}