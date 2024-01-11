package com.example.learnandroid

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.learnandroid.databinding.ActivityBottomTabbarBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity: AppCompatActivity() {
    private lateinit var mBinding: ActivityBottomTabbarBinding

    private val TAG = "BottomTabbarActivity"

    private var mNavigationController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_bottom_tabbar)
        mBinding = ActivityBottomTabbarBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigationView)
        mNavigationController?.let { NavigationUI.setupWithNavController(bottomNavigationView, it) }

        mBinding.navHostFragment.post(Runnable {
            mNavigationController = Navigation.findNavController(mBinding.navHostFragment)
        })
        mBinding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    Log.i(TAG, "0 item : ${it.itemId}")
                    mNavigationController?.navigate(R.id.homeFragment)
                    true
                }
                R.id.navigation_dashboard -> {
                    Log.i(TAG, "1 item : ${it.itemId}")
                    mNavigationController?.navigate(R.id.dashboardFragment)
                    true
                }
                R.id.navigation_mine -> {
                    Log.i(TAG, "2 item : ${it.itemId}")
                    mNavigationController?.navigate(R.id.personFragment)
                    true
                }
                else -> {
                    Log.i(TAG, "3 item : ${it.itemId}")
                    false
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }
}
