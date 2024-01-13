package com.example.learnandroid.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common_module.ext.parseState
import com.example.learnandroid.R
import com.example.learnandroid.base.ui.BaseFragment
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
            ViewModelProvider(this)[HomeViewModel::class.java]
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
        homeViewModel.getHomeList(true)
    }

    private fun bindViews() {
        homeViewModel.getBannerData().observe(this) { it ->
            Log.d("HomeFragment", "getBanner: $it")
            parseState(it, {
                Log.d(TAG, "parseState of it : $it")
                mHomeBannerAdapter = HomeBannerAdapter(it)
                mBanner.setAdapter(mHomeBannerAdapter)
            })
        }

        homeViewModel.getHomeListData().observe(this) {
            Log.d("HomeFragment", "getHomeListData: $it")

            mHomeList.adapter?.let { homeAdapter ->
                if (homeAdapter is HomeListAdapter) {
                    homeAdapter.setData(it.listData)
                }
            }
        }
    }
}