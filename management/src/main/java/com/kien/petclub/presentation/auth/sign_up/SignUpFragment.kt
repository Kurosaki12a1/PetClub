package com.kien.petclub.presentation.auth.sign_up

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kien.petclub.databinding.FragmentSignUpBinding
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.extensions.showMessage
import com.kien.petclub.presentation.auth.AuthActivity
import com.kien.petclub.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    private val viewModel: SignUpViewModel by viewModels()
    override fun getViewBinding(): FragmentSignUpBinding =
        FragmentSignUpBinding.inflate(layoutInflater)

    override fun setUpViews() {
        super.setUpViews()
        binding.ivBack.setOnClickListener { backToPreviousScreen() }
        binding.ivSignUp.setOnClickListener {
            viewModel.signUp(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etRePassword.text.toString(),
                binding.etUserName.text.toString(),
                binding.etPhone.text.toString()
            )
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.signUpResponse.onEach {
            when (it) {
                is Resource.Success -> {
                    (requireActivity() as AuthActivity).stopLoadingAnimation()
                    backToPreviousScreen()
                }

                is Resource.Failure -> {
                    (requireActivity() as AuthActivity).stopLoadingAnimation()
                    showMessage(it.error.message.toString())
                }

                is Resource.Loading -> {
                    (requireActivity() as AuthActivity).showLoadingAnimation()
                }

                else -> {
                    // Do nothing
                }
            }
        }.launchIn(lifecycleScope)

    }

}