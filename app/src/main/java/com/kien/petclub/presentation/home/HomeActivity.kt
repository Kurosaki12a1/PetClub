package com.kien.petclub.presentation.home

import com.kien.petclub.R
import com.kien.petclub.databinding.ActivityHomeBinding
import com.kien.petclub.extensions.setupWithNavController
import com.kien.petclub.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_home

    override fun setUpBottomNavigation() {
        setUpBottomNavigationWithGraphs()
    }

    private fun setUpBottomNavigationWithGraphs() {
        val graphIds = listOf(
            R.navigation.nav_home,
            R.navigation.nav_bill,
            R.navigation.nav_goods,
            R.navigation.nav_notifications,
            R.navigation.nav_more
        )

        val controller = binding.bottomNavigationView.setupWithNavController(
            graphIds,
            supportFragmentManager,
            R.id.fragment_host_container,
            intent
        )

        navController = controller
    }
}