package com.example.learnandroid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learnandroid.R
import com.example.learnandroid.databinding.ActivityMyActivityBinding

class MyActivityA : AppCompatActivity() {

    lateinit var mBinding: ActivityMyActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
//        mBinding =
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_activity)
    }
}