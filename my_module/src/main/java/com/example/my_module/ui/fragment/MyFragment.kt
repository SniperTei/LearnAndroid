package com.example.my_module.ui.fragment

import android.os.Bundle
import com.example.common_library.app.base.fragment.BaseVmFragment
import com.example.common_library.log.LogUtil
import com.example.my_module.R
import com.example.my_module.viewmodel.MyViewModel


class MyFragment : BaseVmFragment<MyViewModel>() {

    private val TAG = "MyFragment"
    override fun layoutId(): Int {
        return R.layout.fragment_my
    }

    override fun initView(savedInstanceState: Bundle?) {
        LogUtil.debugInfo(TAG, "Dashboard fragment")
    }

    override fun lazyLoadData() {
        TODO("Not yet implemented")
    }

    override fun createObserver() {
        TODO("Not yet implemented")
    }

    override fun startLoading(message: String) {
        TODO("Not yet implemented")
    }

    override fun stopLoading() {
        TODO("Not yet implemented")
    }

}