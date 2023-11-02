package com.example.learnandroid.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.learnandroid.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment1 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_blank1, container, false)
        val textView = root.findViewById<TextView>(R.id.hello_fragment_text)
        val okBtn = root.findViewById<Button>(R.id.hello_fragment_btn)
        val forwardBtn = root.findViewById<Button>(R.id.forward_fragment_btn)

        okBtn.setOnClickListener {
            textView.text = "Okay btn clicked and text changed"
        }

        forwardBtn.setOnClickListener {
//            val Intent =
        }

        return root
    }
}