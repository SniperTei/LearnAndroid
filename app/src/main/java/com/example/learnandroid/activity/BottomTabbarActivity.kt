package com.example.learnandroid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.learnandroid.R
import com.example.learnandroid.databinding.ActivityBottomTabbarBinding

class BottomTabbarActivity : AppCompatActivity() {

    lateinit var mBinding: ActivityBottomTabbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBottomTabbarBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}