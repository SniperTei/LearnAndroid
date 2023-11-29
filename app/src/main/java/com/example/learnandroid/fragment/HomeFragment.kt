package com.example.learnandroid.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.learnandroid.R
import com.example.learnandroid.adapter.HomeListAdapter
import com.example.learnandroid.bean.WanResponseBean
import com.example.learnandroid.network.WanAndroidService
import com.example.learnandroid.viewmodel.HomelistViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val TAG = "HomeFragment"
    // viewmodel
    private lateinit var mHomeListViewModel: HomelistViewModel
    // home listview
    // private lateinit var mHomeList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val homeRecycleView = rootView.findViewById<RecyclerView>(R.id.home_list)
        val homeAdapter = activity?.let { HomeListAdapter() }

        val layoutManager = LinearLayoutManager(activity)
        homeRecycleView.layoutManager = layoutManager
        homeRecycleView.adapter = homeAdapter

        mHomeListViewModel = HomelistViewModel()
        mHomeListViewModel.getHomeListApi(0).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : io.reactivex.rxjava3.core.Observer<WanResponseBean> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, "onSubscribe: $d")
                }

                override fun onError(e: Throwable) {
                    Log.d(TAG, "onError: $e")
                }

                override fun onNext(t: WanResponseBean) {
                    Log.d(TAG, "onNext t.data : ${t.data}")
                    // get t.data.datas
                    val data = t.data as Map<*, *>
                    val list = data?.get("datas") as List<*>
                    Log.d(TAG, "onNext list: $list")
                    // use gson convert map to bean
                    val gson = com.google.gson.Gson()
                    val homeListData = ArrayList<com.example.learnandroid.bean.HomeItemBean>()
                    for (item in list) {
                        val json = gson.toJson(item)
                        val homeItemBean = gson.fromJson(json, com.example.learnandroid.bean.HomeItemBean::class.java)
                        homeListData.add(homeItemBean)
                    }
                    Log.d(TAG, "onNext homeListData: $homeListData")
                    homeAdapter?.setData(homeListData)
                }

                override fun onComplete() {
                    Log.d(TAG, "onComplete: ")
                }
            })
        Log.d(TAG, "get home 0")

        return rootView
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
}