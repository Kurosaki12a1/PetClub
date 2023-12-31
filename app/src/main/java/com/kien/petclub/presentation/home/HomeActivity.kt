package com.kien.petclub.presentation.home

import com.kien.petclub.R
import com.kien.petclub.databinding.ActivityHomeBinding
import com.kien.petclub.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun getLayoutId(): Int  = R.layout.activity_home
}