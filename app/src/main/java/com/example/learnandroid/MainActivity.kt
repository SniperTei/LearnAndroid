package com.example.learnandroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.tv_data)
        tv.setText("Hello Android")

        val myBtn = findViewById<Button>(R.id.my_button)
        myBtn.setText("跳转")
        myBtn.setOnClickListener {
            println("myBtn click")
            // 跳转到FirstActivity2 传一个name
            val intent = Intent(this, FirstActivity2::class.java)
            intent.putExtra("name", "zhengnan")
            startActivity(intent)

            // 获取到传过来的参数result
            val result = intent.getStringExtra("result")
            println("result mainact : $result")
        }
        // // 获取到传过来的参数result
        // val result = intent.getStringExtra("result")
        // println("result mainact : $result")

        println("wang de fa")
        val str = "abed"
        for (c in str) {
            println("c : $c")
        }
    }

    override fun onStart() {
        super.onStart()
        println("MainActivity onStart")
    }

    override fun onResume() {
        super.onResume()
        println("MainActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        println("MainActivity onPause")
    }

    override fun onStop() {
        super.onStop()
        println("MainActivity onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("MainActivity onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        println("MainActivity onRestart")
    }
}