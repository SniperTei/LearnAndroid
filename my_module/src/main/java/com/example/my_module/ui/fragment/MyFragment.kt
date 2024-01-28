package com.example.my_module.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.transition.Visibility
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
        val root = view ?: return
        val tv_name = root.findViewById<TextView>(R.id.me_name)
        // 获取 NavController
       val navController =
//        val nsv_my_info = root.findViewById<NestedScrollView>(R.id.nsv_my_info)
        tv_name.setOnClickListener {

            // 展示登录fragment
            val loginFragment = LoginFragment()
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_view_tag, loginFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
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