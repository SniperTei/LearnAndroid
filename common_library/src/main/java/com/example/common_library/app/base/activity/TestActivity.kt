package com.example.common_library.app.base.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.common_module.R

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }
}