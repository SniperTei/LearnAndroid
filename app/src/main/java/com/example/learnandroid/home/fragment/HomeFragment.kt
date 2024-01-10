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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnandroid.R
import com.example.learnandroid.app.base.BaseFragment
import com.example.learnandroid.data.model.bean.WanAndroidResponse
import com.example.learnandroid.home.adapter.HomeBannerAdapter
import com.example.learnandroid.home.model.bean.HomeBannerItemBean
import com.example.learnandroid.home.viewmodel.HomeViewModel
import com.youth.banner.Banner


class HomeFragment: BaseFragment() {

    private val TAG = "HomeFragment"

    private lateinit var homeViewModel: HomeViewModel

   private lateinit var mHomeBannerAdapter: HomeBannerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
        ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)


        val banner = root.findViewById<Banner<HomeBannerItemBean, HomeBannerAdapter>>(R.id.home_banner)
        banner.setBannerRound(20f)


        val bannerLayoutManager = LinearLayoutManager(activity)
        bannerLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        homeViewModel.getBanner().observe(this, Observer<WanAndroidResponse<ArrayList<HomeBannerItemBean>>> {
            Log.d("HomeFragment", "getBanner: $it")
            mHomeBannerAdapter = HomeBannerAdapter(it.data)
            banner.setAdapter(mHomeBannerAdapter)
        })

        homeViewModel.getHomeBanner()

        return root
    }
}