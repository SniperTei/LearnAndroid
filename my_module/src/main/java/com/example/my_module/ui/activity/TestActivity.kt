package com.example.my_module.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.common_library.ext.lifecycle.AppActivityManger
import com.example.my_module.R
import com.example.my_module.ui.fragment.LoginFragment

@Route(path = "/my/test/test-activity")
class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }

//    override fun showLoginPage() {
//        val loginFragment = LoginFragment()
//        // 获取fragmentManager
//        val fragmentManager = AppActivityManger.currentActivity?.supportFragmentManager
//        // 开启事务
//        val transaction = fragmentManager?.beginTransaction()
//        // 添加fragment
//        transaction?.add(R.id.fragment_container_view_tag, loginFragment)
////        // 提交事务
//        transaction?.commit()
//    }
}