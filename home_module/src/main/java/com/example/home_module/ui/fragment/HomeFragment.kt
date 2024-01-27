package com.example.home_module.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common_library.app.base.fragment.BaseVmFragment
import com.example.common_library.ext.loading.startLoadingExt
import com.example.common_library.ext.loading.stopLoadingExt
import com.example.common_library.ext.parseState
import com.example.home_module.R
import com.example.home_module.data.bean.HomeBannerItemBean
import com.example.home_module.ui.adapter.HomeBannerAdapter
import com.example.home_module.ui.adapter.HomeListAdapter
import com.example.home_module.viewmodel.HomeViewModel
import com.example.home_module.viewmodel.RequestHomeVM
import com.youth.banner.Banner


class HomeFragment : BaseVmFragment<HomeViewModel>() {
    private val TAG = "HomeFragment"

    private val mHomeViewModel: RequestHomeVM by viewModels()

    private lateinit var mHomeBannerAdapter: HomeBannerAdapter

    private lateinit var mBanner: Banner<HomeBannerItemBean, HomeBannerAdapter>

    private lateinit var mHomeList: RecyclerView
    override fun layoutId(): Int {
        Log.d(TAG, "layout Id")
        return R.layout.fragment_home
    }

    override fun initView(savedInstanceState: Bundle?) {
        val root = view ?: return
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

    override fun lazyLoadData() {
//        startLoading("loading")
        mHomeViewModel.getHomeBanner()
        mHomeViewModel.getHomeList(true)
    }

    override fun createObserver() {
        Log.d(TAG, "createObserver")
        mHomeViewModel.getBannerData().observe(this) { it ->
            Log.d("HomeFragment", "getBanner: $it")
            parseState(it, {
                Log.d(TAG, "parseState of it : $it")
                mHomeBannerAdapter = HomeBannerAdapter(it)
                mBanner.setAdapter(mHomeBannerAdapter)
            })
        }

        mHomeViewModel.getHomeListData().observe(this) {
            Log.d("HomeFragment", "getHomeListData: $it")

            mHomeList.adapter?.let { homeAdapter ->
                if (homeAdapter is HomeListAdapter) {
                    homeAdapter.setData(it.listData)
                }
            }
        }
    }

    override fun startLoading(message: String) {
        Log.d(TAG, "start loading ")
        startLoadingExt(message)
    }

    override fun stopLoading() {
        Log.d(TAG, "stop loading ")
        stopLoadingExt()
    }

    /*
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

     */
}