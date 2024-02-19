package com.example.core_library.service.my

import com.alibaba.android.arouter.facade.template.IProvider
import org.json.JSONObject

interface MyServiceProvider: IProvider {
    fun getMyInfo(): JSONObject
}