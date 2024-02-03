package com.kien.petclub.presentation.auth.sign_up

import com.kien.petclub.databinding.FragmentSignUpBinding
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {
    override fun getViewBinding(): FragmentSignUpBinding = FragmentSignUpBinding.inflate(layoutInflater)

    override fun setUpViews() {
        super.setUpViews()
        binding.ivBack.setOnClickListener { backToPreviousScreen() }
    }

}