package com.example.forusuistudy.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.example.forusuistudy.R

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
//        val navHostFragment =supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
//        val navController = navHostFragment.navController
    }
}