package com.example.imageeditordemo.ui.main

import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI

import com.example.imageeditordemo.R
import com.example.imageeditordemo.databinding.ActivityMainBinding
import com.example.imageeditordemo.ui.base.BaseActivity

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    override val mViewModel: MainViewModel by viewModels()
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    private lateinit var mNavController: NavController
    private lateinit var mAppBarConfiguration: AppBarConfiguration

    override fun onBinding() {
        setUpNavigationAndActionBar()
    }

    private fun setUpNavigationAndActionBar() {
        setSupportActionBar(mViewBinding.toolbar)
        mNavController = this.findNavController(R.id.nav_host_fragment)
        mAppBarConfiguration = AppBarConfiguration(mNavController.graph)

        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration)
                || super.onSupportNavigateUp()
    }
}