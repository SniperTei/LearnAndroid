package com.example.learnandroid.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.learnandroid.R
import com.example.learnandroid.databinding.ActivityMyActivityBinding

class MyActivityA : AppCompatActivity() {

    lateinit var mBinding: ActivityMyActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        mBinding = ActivityMyActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my_activity) // dead
        setContentView(mBinding.root)

        mBinding.heiheiBtn.setOnClickListener {
            Log.i("MyActivityA", "heihei btn clicked")
            val text = "changed name"
            mBinding.myActivityText.text = "hello there"
        }

        mBinding.myActivityText.setOnClickListener {
            Log.i("MyActivityA", "my activity text clicked!")
        }
    }
}