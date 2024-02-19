package com.example.my_module.service.impl

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.common_library.log.LogUtil
import com.example.core_library.service.my.MyServiceProvider
import com.example.my_module.data.bean.MyInfoBean
import com.google.gson.Gson
import org.json.JSONObject

@Route(path = "/myModule/myService", name = "我的相关服务")
class MyServiceImpl: MyServiceProvider {

    private val TAG = "MyServiceImpl"

    private val mMyInfo = MyInfoBean("Sniper", 9999, 2, 100000, "zhengnan")

    override fun getMyInfo(): JSONObject {
        val gson = Gson()
        val jsonString = gson.toJson(mMyInfo)
        return JSONObject(jsonString)
    }

    override fun init(context: Context?) {
        LogUtil.debugInfo(TAG, "my service impl init")
    }
}