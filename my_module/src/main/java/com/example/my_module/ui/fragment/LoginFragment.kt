package com.example.my_module.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.core_library.ui.fragment.BaseVmFragment
import com.example.common_library.log.LogUtil
import com.example.my_module.R
import com.example.my_module.viewmodel.state.LoginViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : BaseVmFragment<LoginViewModel>() {

    private val TAG = "LoginFragment"
    override fun layoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initView(savedInstanceState: Bundle?) {
        LogUtil.debugInfo(TAG, "initView")
    }

    override fun lazyLoadData() {
        LogUtil.debugInfo(TAG, "lazyLoadData")
    }

    override fun createObserver() {
        LogUtil.debugInfo(TAG, "createObserver")
    }

    override fun startLoading(message: String) {

    }

    override fun stopLoading() {

    }

}