package com.example.learnandroid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.learnandroid.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_my, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        println("MyFragment onDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("MyFragment onDestroyView")
    }

    override fun onDetach() {
        super.onDetach()
        println("MyFragment onDetach")
    }

    override fun onPause() {
        super.onPause()
        println("MyFragment onPause")
    }

    override fun onResume() {
        super.onResume()
        println("MyFragment onResume")
    }

    override fun onStart() {
        super.onStart()
        println("MyFragment onStart")
    }

    override fun onStop() {
        super.onStop()
        println("MyFragment onStop")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("MyFragment onViewCreated")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        println("MyFragment onHiddenChanged")
    }

    override fun onLowMemory() {
        super.onLowMemory()
        println("MyFragment onLowMemory")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        println("MyFragment onSaveInstanceState")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        println("MyFragment onViewStateRestored")
    }
}