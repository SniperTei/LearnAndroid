package com.example.learnandroid.dashboard.fragment

import android.os.Bundle
import android.util.Log
import com.example.common_module.base.fragment.BaseVmFragment
import com.example.common_module.ext.loading.startLoadingExt
import com.example.common_module.ext.loading.stopLoadingExt
import com.example.learnandroid.R
import com.example.learnandroid.dashboard.viewmodel.DashboardVM

class DashboardFragment: BaseVmFragment<DashboardVM>() {

    private val TAG = "DashboardFragment"
    override fun layoutId(): Int {
        return R.id.dashboardFragment
    }

    override fun initView(savedInstanceState: Bundle?) {
        Log.d(TAG, "initView dashboard fragment")
    }

    override fun lazyLoadData() {
        Log.d(TAG, "lazy loaddata")
    }

    override fun createObserver() {
        Log.d(TAG, "createObserver")
    }

    override fun startLoading(message: String) {
        startLoadingExt(message)
    }

    override fun stopLoading() {
        stopLoadingExt()
    }
}