package com.kien.petclub.presentation.auth.forgot_password

import com.kien.petclub.databinding.FragmentForgotPasswordBinding
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    override fun getViewBinding(): FragmentForgotPasswordBinding = FragmentForgotPasswordBinding.inflate(layoutInflater)

    override fun setUpViews() {
        super.setUpViews()
        binding.ivBack.setOnClickListener { backToPreviousScreen() }
    }
}