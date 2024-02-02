package com.kien.petclub.presentation.login

import com.kien.petclub.databinding.ActivityLoginBinding
import com.kien.petclub.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    override fun getViewBinding(): ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
}