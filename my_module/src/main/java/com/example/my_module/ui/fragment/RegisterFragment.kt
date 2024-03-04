package com.example.my_module.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.common_library.ext.loading.startLoadingExt
import com.example.common_library.ext.loading.stopLoadingExt
import com.example.core_library.ui.fragment.BaseVmFragment
import com.example.my_module.R
import com.example.my_module.viewmodel.state.LoginViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : BaseVmFragment<LoginViewModel>() {
    override fun layoutId(): Int {
        return R.layout.fragment_register
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {

    }

    override fun startLoading(message: String) {
        startLoadingExt(message)
    }

    override fun stopLoading() {
        stopLoadingExt()
    }

}