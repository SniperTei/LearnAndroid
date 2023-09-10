package com.example.learnandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    var mTextView: TextView? = null

    var mDiceImg: ImageView? = null

    private val myApplication: MyApplication by lazy {
        application as MyApplication
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 点数
        mTextView = findViewById<TextView>(R.id.tv_data)
        // 骰子图片
        mDiceImg = findViewById<ImageView>(R.id.diceImg)

        val rollBtn: Button = findViewById(R.id.roll_btn)
        rollBtn.setOnClickListener {
            Log.i(TAG, "roll btn click")
            Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_SHORT).show()
            rollDice()
        }
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

    private fun rollDice() {
        val dice = Dice(6)
        val result = dice.roll()
        mTextView?.text = result.toString()
        if (result == 1) {
            mDiceImg?.setImageResource(R.drawable.dice_1)
        } else if (result == 2) {
            mDiceImg?.setImageResource(R.drawable.dice_2)
        } else if (result == 3) {
            mDiceImg?.setImageResource(R.drawable.dice_3)
        } else if (result == 4) {
            mDiceImg?.setImageResource(R.drawable.dice_4)
        } else if (result == 5) {
            mDiceImg?.setImageResource(R.drawable.dice_5)
        } else if (result == 6) {
            mDiceImg?.setImageResource(R.drawable.dice_6)
        }
    }
}

class Dice(val number: Int) {
    fun roll(): Int {
        return (1..6).random()
    }
}