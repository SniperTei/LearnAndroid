package com.example.learnandroid.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.learnandroid.R
import com.example.learnandroid.network.HttpbinService
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val mOkHttpClient: OkHttpClient = OkHttpClient()

    private lateinit var mHttpbinService: HttpbinService

    private lateinit var mRetrofit: Retrofit

    // TAG
    private val TAG = "BlankFragment2"

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment2.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment2().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
    }

    //
    override fun onAttachFragment(childFragment: Fragment) {
        // super.onAttachFragment(childFragment)
        Log.d(TAG, "onAttachFragment")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        Log.d(TAG, "onCreate")
        mRetrofit = Retrofit.Builder()
            .baseUrl("https://www.httpbin.org/")
            .build()

        mHttpbinService = mRetrofit.create(HttpbinService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView")
        val rootView = inflater.inflate(R.layout.fragment_blank2, container, false)
//        val imageView = rootView.findViewById(R.id.blank2_frag_imgView) as ImageView

        val getSyncBtn = rootView.findViewById<Button>(R.id.get_sync_btn)
        getSyncBtn.setOnClickListener {
            Log.d(TAG, "get Sync btn clicked")
            val httpBinUrl = "https://www.httpbin.org/get?name=zhengnan&age=33"
            getSync(httpBinUrl)
        }

        val getAsyncBtn = rootView.findViewById<Button>(R.id.get_async_btn)
        getAsyncBtn.setOnClickListener {
            Log.d(TAG, "get Async btn clicked")
            val httpBinUrl = "https://www.httpbin.org/get"
            // okhttp3
//            getAsync(httpBinUrl)
            // retrofit
            getAsyncRetrofit()
        }

        val postSyncBtn = rootView.findViewById<Button>(R.id.post_sync_btn)
        postSyncBtn.setOnClickListener {
            Log.d(TAG, "post Sync btn clicked")
            
            val httpBinUrl = "https://www.httpbin.org/post"
            postSync(httpBinUrl)
        }

        val postAsyncBtn = rootView.findViewById<Button>(R.id.post_async_btn)
        postAsyncBtn.setOnClickListener {
            Log.d(TAG, "post Async btn clicked")
            val httpBinUrl = "https://www.httpbin.org/post"
            postAsync(httpBinUrl)
        }

        return rootView
    }

    // okhttp3 get sync code
    private fun getSync(url: String) {
        // okhttp3 get sync code 新开一个thread
        Thread(Runnable {
            val request = Request.Builder()
                .url(url)
                .build()
            try {
                val response = mOkHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    Log.d(TAG, "getSync response: ${response.body?.string()}")
                } else {
                    Log.d(TAG, "getSync failed")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }

    // okhttp3 get async code
    private fun getAsync(url: String) {
        // okhttp3 get async code
        val request = Request.Builder()
            .url(url)
            .build()
        mOkHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d(TAG, "getAsync failed")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    Log.d(TAG, "getAsync response: ${response.body?.string()}")
                } else {
                    Log.d(TAG, "getAsync failed")
                }
            }
        })
    }

    private fun getAsyncRetrofit() {
        // retrofit get async code
        val call = mHttpbinService.get("zhengnan", 33)
        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "getAsyncRetrofit failed")
            }

            override fun onResponse(
                call: retrofit2.Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, "getAsyncRetrofit response: ${response.body()?.string()}")
                } else {
                    Log.d(TAG, "getAsyncRetrofit failed")
                }
            }
        })
    }

    // okhttp3 post sync code
    private fun postSync(url: String) {
        // okhttp3 post sync code
        Thread(Runnable {
            val request = Request.Builder()
                .url(url)
                .post(okhttp3.RequestBody.create(null, ""))
                .build()
            try {
                val response = mOkHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    Log.d(TAG, "postSync response: ${response.body?.string()}")
                } else {
                    Log.d(TAG, "postSync failed")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }

    // okhttp3 post async code
    private fun postAsync(url: String) {
        // okhttp3 post async code
        val request = Request.Builder()
            .url(url)
            .post(okhttp3.RequestBody.create(null, ""))
            .build()
        mOkHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d(TAG, "postAsync failed")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    Log.d(TAG, "postAsync response: ${response.body?.string()}")
                } else {
                    Log.d(TAG, "postAsync failed")
                }
            }
        })
    }

    // life cycle

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d(TAG, "onActivityCreated")
    }

    // 生命周期
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        // 保存数据
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    //
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    // 
    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach")
    }

    // 保存数据
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
    }

    // 恢复数据
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAG, "onViewStateRestored")
    }

    //
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d(TAG, "onHiddenChanged : $hidden")
    }
}