package com.example.learnandroid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.learnandroid.R
import com.example.learnandroid.adapter.MyAAdapter
import com.example.learnandroid.bean.MyABean
import com.example.learnandroid.databinding.ActivityMyActivityBinding

class MyActivityA : AppCompatActivity() {

    private lateinit var data: List<MyABean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_activity)

        // for循环30次
        data = (0..30).map {
            MyABean("name", it)
        }
        Log.i("Sniper", "aaa : $data")

        val myRecyclerView = findViewById<RecyclerView>(R.id.my_rv)
        val myAAdapter = MyAAdapter(this, data)
////        val layoutManager = GridLayoutManager(this, 3)
        val layoutManager = LinearLayoutManager(this)
        myRecyclerView.layoutManager = layoutManager
//
        myRecyclerView.adapter = myAAdapter
        myAAdapter.setOnItemClickListener(object : MyAAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                Log.i("Sniper", "item clicked: $position")
            }
        })

        // 另一种方式 创建一个textview
//        val layout = LinearLayout(this)
//        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        layout.layoutParams = layoutParams
//
//        val textView: TextView = TextView(this)
//        textView.text = "This is a text"
//        layout.addView(textView)
//        setContentView(layout)
    }


//    lateinit var mBinding: ActivityMyActivityBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        mBinding = ActivityMyActivityBinding.inflate(layoutInflater)
//        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_my_activity) // dead
//        setContentView(mBinding.root)
//
//        mBinding.heiheiBtn.setOnClickListener {
//            Log.i("MyActivityA", "heihei btn clicked")
//            val text = "changed name"
//            mBinding.myActivityText.text = "hello there"
//        }
//
//        mBinding.myActivityText.setOnClickListener {
//            Log.i("MyActivityA", "my activity text clicked!")
//        }
//    }
}