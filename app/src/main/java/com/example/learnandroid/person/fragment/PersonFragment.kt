package com.example.learnandroid.person.fragment

import android.os.Bundle
import android.util.Log
import com.example.common_module.base.fragment.BaseVmFragment
import com.example.learnandroid.R
import com.example.learnandroid.person.viewmodel.PersomVM

class PersonFragment: BaseVmFragment<PersomVM>() {

    private val TAG = "PersonFragment"
    override fun layoutId(): Int {
        return R.id.personFragment
    }

    override fun initView(savedInstanceState: Bundle?) {
        Log.d(TAG, "initView person fragment")
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