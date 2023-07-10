package com.example.learnandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    private val myApplication: MyApplication by lazy {
        application as MyApplication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.tv_data)
        tv.text = "Hello Android"
        tv.textSize = 30f
        tv.setTextColor(0xffff0000.toInt())
        tv.setBackgroundColor(0xff00ff00.toInt())

        val myBtn = findViewById<Button>(R.id.my_button)
        myBtn.text = "跳转"
        myBtn.setOnClickListener {
            Log.i(TAG, "jump btn click")
            // 跳转到FirstActivity2 传一个name
            val intent = Intent(this, FirstActivity2::class.java)
            intent.putExtra("name", "zhengnan")
            startActivity(intent)

            // 获取到传过来的参数result
            val result = intent.getStringExtra("result")
            Log.i(TAG, "result = $result")
        }
        // // 获取到传过来的参数result
        // val result = intent.getStringExtra("result")
        // println("result mainact : $result")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "MainActivity onStart")
        
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "MainActivity onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "MainActivity onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "MainActivity onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "MainActivity onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "MainActivity onRestart")
    }
}