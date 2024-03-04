package com.example.my_module.ui.fragment

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.alibaba.android.arouter.launcher.ARouter
import com.example.core_library.ui.fragment.BaseVmFragment
import com.example.common_library.log.LogUtil
import com.example.core_library.service.login.LoginProvider
import com.example.core_library.service.my.MyServiceProvider
import com.example.my_module.R
import com.example.my_module.viewmodel.request.MyRequestViewModel
import com.example.my_module.viewmodel.state.MyViewModel


class MyFragment : BaseVmFragment<MyViewModel>() {

    private val TAG = "MyFragment"

    private val myRequestViewModel: MyRequestViewModel by viewModels()

    private val mLoginImpl: LoginProvider = ARouter.getInstance().build("/myModule/loginImpl").navigation() as LoginProvider
    override fun layoutId(): Int {
        return R.layout.fragment_my
    }

    override fun initView(savedInstanceState: Bundle?) {
        LogUtil.debugInfo(TAG, "My fragment")
        val root = view ?: return
        val tv_name = root.findViewById<TextView>(R.id.me_name)
        // 获取 NavController
       val navController =
//        val nsv_my_info = root.findViewById<NestedScrollView>(R.id.nsv_my_info)
        tv_name.setOnClickListener {
            LogUtil.debugInfo(TAG, "tv name clicked")
            val manager = mActivity.supportFragmentManager
            mLoginImpl.showLoginPage(supportFragmentManager = manager)
//            ARouter.getInstance().build("/home/test/home-test-activity").navigation()
        }
    }

    override fun lazyLoadData() {
        LogUtil.debugInfo(TAG, "lazyLoadData")
        myRequestViewModel.getMyInfo()
    }

    override fun createObserver() {
        LogUtil.debugInfo(TAG, "createObserver")
    }

    override fun startLoading(message: String) {

    }

    override fun stopLoading() {

    }

}