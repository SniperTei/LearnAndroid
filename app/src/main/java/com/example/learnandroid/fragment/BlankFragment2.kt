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
import okhttp3.OkHttpClient
import okhttp3.Request
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
            val httpBinUrl = "https://www.httpbin.org/get"
            getSync(httpBinUrl)
        }

        return rootView
    }

    // okhttp3
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