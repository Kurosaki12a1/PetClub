package com.kien.petclub.presentation.auth.sign_in

import com.kien.petclub.databinding.FragmentSignInBinding
import com.kien.petclub.extensions.navigateSafe
import com.kien.petclub.extensions.openActivity
import com.kien.petclub.presentation.base.BaseFragment
import com.kien.petclub.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    override fun getViewBinding(): FragmentSignInBinding =
        FragmentSignInBinding.inflate(layoutInflater)


    override fun setUpViews() {
        super.setUpViews()

        binding.ivLogin.setOnClickListener { openHome() }

        binding.tvForgotPassword.setOnClickListener { openForgotPassword() }

        binding.ivRegister.setOnClickListener { openSignUp() }
    }


    private fun openForgotPassword() {
        navigateSafe(SignInFragmentDirections.actionOpenForgotPasswordFragment())
    }

    private fun openSignUp() {
        navigateSafe(SignInFragmentDirections.actionOpenSignUpFragment())
    }

    private fun openHome() {
        openActivity(HomeActivity::class.java)
        requireActivity().finish()
    }

}