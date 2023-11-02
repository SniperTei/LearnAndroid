package com.example.learnandroid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.learnandroid.R
import com.example.learnandroid.fragment.BlankFragment2
import com.example.learnandroid.fragment.ItemFragment1

//class MyActivityB : AppCompatActivity(), OnClickListener {
class MyActivityB : AppCompatActivity(), OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_b)

        val aBtn = findViewById<Button>(R.id.activity_b_btn_a)
        val bBtn = findViewById<Button>(R.id.activity_b_btn_b)

        aBtn.setOnClickListener(this)
        bBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val clickBtn: Button = v as Button
        Log.i("Sniper", "clicked ： ${clickBtn.text}")

        if ("A Btn" == clickBtn.text) { // 点了A按钮
            replaceFragmentBelowWithFragment(BlankFragment2())
        } else {
            replaceFragmentBelowWithFragment(ItemFragment1())
        }
    }

    private fun replaceFragmentBelowWithFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        // 替换成fragment
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_placeholder, fragment)
        transaction.commit()
    }
}
