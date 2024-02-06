package com.example.dashboard_module.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.core_library.ui.fragment.BaseVmFragment
import com.example.common_library.log.LogUtil
import com.example.dashboard.R
import com.example.dashboard_module.viewmodel.DashboardViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : BaseVmFragment<DashboardViewModel>() {

    private val TAG = "DashboardFragment"
    override fun layoutId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun initView(savedInstanceState: Bundle?) {
        LogUtil.debugInfo(TAG, "Dashboard fragment")
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