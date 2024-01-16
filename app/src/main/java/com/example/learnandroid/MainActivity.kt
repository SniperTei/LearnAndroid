package com.example.learnandroid

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity: AppCompatActivity() {
//    private lateinit var mBinding: ActivityBottomTabbarBinding

    private val TAG = "BottomTabbarActivity"

    private var mNavigationController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        mBinding = ActivityBottomTabbarBinding.inflate(layoutInflater)
//        setContentView(mBinding.root)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigationView)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        mNavigationController = navHostFragment.navController

        bottomNavigationView.setOnItemSelectedListener {
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
