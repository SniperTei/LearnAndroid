package com.example.home_module.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.common_library.log.LogUtil
import com.example.core_library.service.my.MyServiceProvider
import com.example.home_module.R

@Route(path = "/home/test/home-test-activity")
class HomeTestActivity : AppCompatActivity() {

    private val TAG = "HomeTestActivity"

    private val mMyService: MyServiceProvider = ARouter.getInstance().build("/myModule/myService").navigation() as MyServiceProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_test)

        val myInfo = mMyService.getMyInfo()

        LogUtil.debugInfo(TAG, "my info name : ${myInfo.get("name")}")
    }
}