package com.kien.petclub.presentation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    abstract fun getViewBinding(): VB

    lateinit var navController: LiveData<NavController>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        if (savedInstanceState == null) {
            setUpBottomNavigation()
        }
        setUpViews()
        setUpObserver()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setUpBottomNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.value?.navigateUp()!! || super.onSupportNavigateUp()
    }

    open fun setUpBottomNavigation() {}

    open fun setUpViews() {}

    open fun setUpObserver() {}
}