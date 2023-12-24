package com.example.learnandroid.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnandroid.R
import com.example.learnandroid.adapter.HomeBannerAdapter
import com.example.learnandroid.adapter.HomeListAdapter
import com.example.learnandroid.bean.HomeBannerItemBean
import com.example.learnandroid.bean.WanResponseBean
import com.example.learnandroid.viewmodel.HomeViewModel
import com.google.gson.Gson
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val TAG = "HomeFragment"
    // viewmodel
    private var mHomeListViewModel: HomeViewModel? = null
    // home listview
    // private lateinit var mHomeList

    private var mHomeBanner: Banner<HomeBannerItemBean, HomeBannerAdapter>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
//        lifecycle.addObserver(MyObserver())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        // 首页banner
        val bannerList = ArrayList<HomeBannerItemBean>()
        val homeBannerAdapter = activity?.let { HomeBannerAdapter(bannerList) }
        val mHomeBanner = rootView.findViewById<Banner<HomeBannerItemBean, HomeBannerAdapter>>(R.id.home_banner)
        mHomeBanner.setBannerRound(20f)
        mHomeBanner.isAutoLoop(true)
        val circleIndicator = CircleIndicator(activity)
        mHomeBanner.indicator = circleIndicator
        mHomeBanner.setAdapter(homeBannerAdapter, true)

        val bannerLayoutManager = LinearLayoutManager(activity)
        bannerLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        if (homeBannerAdapter != null) {
            bindBannerData(homeBannerAdapter)
        }
        
        // 首页列表
//        val homeRecycleView = rootView.findViewById<RecyclerView>(R.id.home_list)
//        val homeAdapter = activity?.let { HomeListAdapter() }
//
//        val layoutManager = LinearLayoutManager(activity)
//        homeRecycleView.layoutManager = layoutManager
//        homeRecycleView.adapter = homeAdapter

        mHomeListViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        if (mHomeListViewModel != null) {
            mHomeListViewModel?.getBannerApi()
        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
        mHomeBanner?.destroy()
    }

    override fun onPause() {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onResume() {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onStart() {
        Log.d(TAG, "onStart")
        super.onStart()
        mHomeBanner?.start()
        
    }

    override fun onStop() {
        Log.d(TAG, "onStop")
        super.onStop()
        mHomeBanner?.stop()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun bindBannerData(homeBannerAdapter: HomeBannerAdapter) {
        mHomeListViewModel?.getHomeBannerLiveData()?.observe(this, Observer {
            Log.d(TAG, "bindBannerData it.data: ${it.data}")
            val list = it.data as List<Map<*, *>>
            Log.d(TAG, "bindBannerData list: $list")
            // use gson convert map to bean
            val gson = Gson()
            val bannerListData = ArrayList<HomeBannerItemBean>()
            for (item in list) {
                val json = gson.toJson(item)
                val bannerItemBean = gson.fromJson(json, HomeBannerItemBean::class.java)
                bannerListData.add(bannerItemBean)
            }
            homeBannerAdapter.setDatas(bannerListData)
        })
    }

//    private fun getHomeList(homeAdapter: HomeListAdapter) {
//        mHomeListViewModel.getHomeListApi(0).subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<WanResponseBean> {
//                override fun onSubscribe(d: Disposable) {
//                    Log.d(TAG, "getHomeList onSubscribe: $d")
//                }
//
//                override fun onError(e: Throwable) {
//                    Log.d(TAG, "getHomeList onError: $e")
//                }
//
//                override fun onNext(t: WanResponseBean) {
//                    Log.d(TAG, "onNext t.data : ${t.data}")
//                    // get t.data.datas
//                    val data = t.data as Map<*, *>
//                    val list = data?.get("datas") as List<*>
//                    Log.d(TAG, "onNext list: $list")
//                    // use gson convert map to bean
//                    val gson = com.google.gson.Gson()
//                    val homeListData = ArrayList<com.example.learnandroid.bean.HomeListItemBean>()
//                    for (item in list) {
//                        val json = gson.toJson(item)
//                        val homeItemBean = gson.fromJson(json, com.example.learnandroid.bean.HomeListItemBean::class.java)
//                        homeListData.add(homeItemBean)
//                    }
//                    Log.d(TAG, "getHomeList onNext homeListData: $homeListData")
//                    homeAdapter.setData(homeListData)
//                }
//
//                override fun onComplete() {
//                    Log.d(TAG, "getHomeList onComplete: ")
//                }
//            })
//        Log.d(TAG, "get home 0")
//    }
//
//    private fun getBanner(bannerAdapter: HomeBannerAdapter) {
//        mHomeListViewModel.getBannerApi().subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Observer<WanResponseBean> {
//                override fun onSubscribe(d: Disposable) {
//                    Log.d(TAG, "getBanner onSubscribe: $d")
//                }
//
//                override fun onError(e: Throwable) {
//                    Log.d(TAG, "getBanner onError: $e")
//                }
//
//                override fun onNext(t: WanResponseBean) {
//                    Log.d(TAG, "getBanner onNext t.data : ${t.data}")
//                    val list = t.data as List<Map<*, *>>
//                    Log.d(TAG, "onNext list: $list")
//                    // use gson convert map to bean
//                    val gson = com.google.gson.Gson()
//                    val bannerListData = ArrayList<HomeBannerItemBean>()
//                    for (item in list) {
//                        val json = gson.toJson(item)
//                        val bannerItemBean = gson.fromJson(json, HomeBannerItemBean::class.java)
//                        bannerListData.add(bannerItemBean)
//                    }
//                    bannerAdapter.setDatas(bannerListData)
//                }
//
//                override fun onComplete() {
//                    Log.d(TAG, "getBanner onComplete: ")
//                }
//            })
//        Log.d(TAG, "getBanner")
//    }
}