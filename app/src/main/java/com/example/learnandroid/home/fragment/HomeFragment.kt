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
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroid.R
import com.example.learnandroid.app.base.BaseFragment
import com.example.learnandroid.data.model.bean.WanAndroidResponse
import com.example.learnandroid.home.adapter.HomeBannerAdapter
import com.example.learnandroid.home.adapter.HomeListAdapter
import com.example.learnandroid.home.model.bean.HomeBannerItemBean
import com.example.learnandroid.home.viewmodel.HomeViewModel
import com.youth.banner.Banner


class HomeFragment: BaseFragment() {

    private val TAG = "HomeFragment"

    private lateinit var homeViewModel: HomeViewModel

    private lateinit var mHomeBannerAdapter: HomeBannerAdapter

    private lateinit var mBanner: Banner<HomeBannerItemBean, HomeBannerAdapter>

    private lateinit var mHomeList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
        ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // view
        initView(root)

        // bind
        bindViews()

        // data
        initData()

        return root
    }

    private fun initView(root: View) {
        mBanner = root.findViewById<Banner<HomeBannerItemBean, HomeBannerAdapter>>(R.id.home_banner)
        mBanner.setBannerRound(20f)
        val bannerLayoutManager = LinearLayoutManager(activity)
        bannerLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        // 首页列表
        mHomeList = root.findViewById<RecyclerView>(R.id.home_list)
        val homeAdapter = activity?.let { HomeListAdapter() }

        val layoutManager = LinearLayoutManager(activity)
        mHomeList.layoutManager = layoutManager
        mHomeList.adapter = homeAdapter
    }

    // data
    private fun initData() {
        homeViewModel.getHomeBanner()
        homeViewModel.getHomeList()
    }

    private fun bindViews() {
        homeViewModel.getBanner().observe(this, Observer<WanAndroidResponse<ArrayList<HomeBannerItemBean>>> {
            Log.d("HomeFragment", "getBanner: $it")
            mHomeBannerAdapter = HomeBannerAdapter(it.data)
            mBanner.setAdapter(mHomeBannerAdapter)
        })

        homeViewModel.getHomeListData().observe(this, Observer<WanAndroidResponse<com.example.learnandroid.home.model.bean.HomeDataBean<ArrayList<com.example.learnandroid.home.model.bean.HomeListItemBean>>>> {
            Log.d("HomeFragment", "getHomeListData: $it")
            mHomeList.adapter?.let { homeAdapter ->
                if (homeAdapter is HomeListAdapter) {
                    homeAdapter.setData(it.data.datas)
                }
            }
        })
    }
}