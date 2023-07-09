package com.example.learnandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.learnandroid.fragment.MyFragment

class FirstActivity2 : AppCompatActivity() {

    private val TAG = "FirstActivity2"

    private var mFragment: MyFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first2)
        println("FirstActivity2 onCreate")

        // 获取到传过来的参数name
        val name = intent.getStringExtra("name")
        Log.i(TAG, "name = $name")
//        // 返回按钮
//        val backBtn = findViewById<Button>(R.id.btn_back)
//        backBtn.setOnClickListener {
//            // 返回上一个页面带上一个参数
//            val intent = Intent()
//            intent.putExtra("result", "Hello MainActivity")
//            setResult(RESULT_OK, intent)
//            finish()
//        }

        mFragment = MyFragment()
        Log.i(TAG, "myFragment = $mFragment")

        // fragmentManager
        val fragmentManager = supportFragmentManager
        Log.i(TAG, "fragmentManager = $fragmentManager")
        // 开启一个事务
        val transaction = fragmentManager.beginTransaction()
        Log.i(TAG, "transaction = $transaction")
        // 将fragment添加到布局中
        transaction.add(R.id.fl_fragment_container, mFragment!!)
        // 提交事务
        transaction.commit()

    }

    override fun onStart() {
        super.onStart()
        println("FirstActivity2 onStart")
    }    

    override fun onResume() {
        super.onResume()
        println("FirstActivity2 onResume")
    }

    override fun onPause() {
        super.onPause()
        println("FirstActivity2 onPause")
    }

    override fun onStop() {
        super.onStop()
        println("FirstActivity2 onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("FirstActivity2 onDestroy")
    }
}