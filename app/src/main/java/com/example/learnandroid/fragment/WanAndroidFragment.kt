package com.example.learnandroid.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.learnandroid.R
import com.example.learnandroid.bean.ErrorBean
import com.example.learnandroid.network.WanAndroidService
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WanAndroidFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WanAndroidFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private  lateinit var mWanAndroidService: WanAndroidService

    private lateinit var mRetrofit: Retrofit

    // TAG
    private val TAG = "WanAndroidFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        mRetrofit = Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com/")
            .build()
        mWanAndroidService = mRetrofit.create(WanAndroidService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_wan_android, container, false)
        val loginBtn = rootView.findViewById<Button>(R.id.wanandroid_login_btn)
        loginBtn.setOnClickListener {
            loginWanAndroid()
        }
        return rootView
    }

    private fun loginWanAndroid() {
        val call = mWanAndroidService.login("lanceedu", "123123")
        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val strJson = response.body()?.string()
                    Log.d(TAG, "loginWanAndroid response: $strJson")
                    val errorBean = Gson().fromJson(strJson, ErrorBean::class.java)
                    Log.d(TAG, "errorBean.code ${errorBean.errorCode}")
                    Log.d(TAG, "errorBean.msg ${errorBean.errorMsg}")
                } else {
                    Log.d(TAG, "loginWanAndroid failed: ${response.body()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "getAsyncRetrofit failed")
            }

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WanAndroidFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WanAndroidFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}