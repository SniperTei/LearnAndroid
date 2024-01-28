package com.example.my_module.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.example.common_library.app.base.fragment.BaseVmFragment
import com.example.common_library.log.LogUtil
import com.example.my_module.R
import com.example.my_module.viewmodel.MyViewModel


class MyFragment : BaseVmFragment<MyViewModel>() {

    private val TAG = "MyFragment"

    private val myViewModel: MyViewModel by viewModels()
    override fun layoutId(): Int {
        return R.layout.fragment_my
    }

    override fun initView(savedInstanceState: Bundle?) {
        LogUtil.debugInfo(TAG, "My fragment")
    }

    override fun lazyLoadData() {
        LogUtil.debugInfo(TAG, "lazyLoadData")
        myViewModel.getMyInfo()
    }

    override fun createObserver() {
        LogUtil.debugInfo(TAG, "createObserver")
    }

    override fun startLoading(message: String) {

    }

    override fun stopLoading() {

    }

}