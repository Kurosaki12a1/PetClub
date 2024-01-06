package com.kien.petclub.presentation.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    private var viewBinding: VB? = null

    open val binding get() = viewBinding!!

    lateinit var navController: LiveData<NavController>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpViews()
        if (savedInstanceState == null) {
            setUpBottomNavigation()
        }
    }

    override
    fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setUpBottomNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.value?.navigateUp()!! || super.onSupportNavigateUp()
    }


    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun setUpBottomNavigation() {}

    open fun setUpViews() {}
}