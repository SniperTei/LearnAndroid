package com.example.learnandroid.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.learnandroid.R
import com.example.learnandroid.app.base.BaseFragment
import com.example.learnandroid.data.model.bean.WanAndroidResponse
import com.example.learnandroid.home.model.bean.HomeBannerItemBean
import com.example.learnandroid.home.viewmodel.HomeViewModel


class HomeFragment: BaseFragment() {

    private val TAG = "HomeFragment"

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
        ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.title)
        homeViewModel.getTitleName().observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        val changeTitleBtn: Button = root.findViewById(R.id.changeTitleBtn)
        changeTitleBtn.setOnClickListener {
//            homeViewModel.changeTitleName("Home Fragment")
            homeViewModel.getHomeBanner()
        }

        homeViewModel.getBanner().observe(this, Observer<WanAndroidResponse<ArrayList<HomeBannerItemBean>>> {
            Log.d("HomeFragment", "getBanner: $it")
        })

        homeViewModel.getHomeBanner()

        return root
    }
}