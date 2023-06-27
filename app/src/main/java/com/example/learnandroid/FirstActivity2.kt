package com.example.learnandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class FirstActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first2)
        println("FirstActivity2 onCreate")
        // 获取到传过来的参数name
        val name = intent.getStringExtra("name")
        println("name : $name")
        // 返回按钮
        val backBtn = findViewById<Button>(R.id.btn_back)
        backBtn.setOnClickListener {
            // 返回上一个页面带上一个参数
            val intent = Intent()
            intent.putExtra("result", "Hello MainActivity")
            setResult(RESULT_OK, intent)
            finish()
        }

    }
}