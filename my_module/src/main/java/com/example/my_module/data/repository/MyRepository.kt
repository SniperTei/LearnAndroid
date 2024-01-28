package com.example.my_module.data.repository

import com.example.common_library.app.base.bean.AppResponse
import com.example.my_module.data.bean.MyInfoBean
import com.example.my_module.data.repository.server.myService

class MyRepository {
    companion object {
        val INSTANCE: MyRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            MyRepository()
        }
    }

    suspend fun getMyInfoFromServer(): AppResponse<MyInfoBean> {
        return myService.getMyInfo()
    }
}