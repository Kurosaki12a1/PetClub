package com.kien.petclub.presentation.auth.forgot_password

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kien.petclub.databinding.FragmentForgotPasswordBinding
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.presentation.auth.AuthActivity
import com.kien.petclub.presentation.base.BaseFragment
import com.kien.petclub.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>() {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun getViewBinding(): FragmentForgotPasswordBinding =
        FragmentForgotPasswordBinding.inflate(layoutInflater)

    override fun setUpViews() {
        super.setUpViews()
        binding.ivBack.setOnClickListener { backToPreviousScreen() }
        binding.ivConfrm.setOnClickListener {
            val email = binding.etEmail.toString()
            viewModel.recoverPassword(email)
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.recoverPasswordResponse.onEach {
            when (it) {
                is Resource.Success -> {
                    (requireActivity() as AuthActivity).stopLoadingAnimation()
                    showMessage(requireActivity(), "Email sent!")
                }

                is Resource.Loading -> {
                    (requireActivity() as AuthActivity).showLoadingAnimation()
                }

                is Resource.Failure -> {
                    (requireActivity() as AuthActivity).stopLoadingAnimation()
                    showMessage(requireActivity(), it.error.message.toString())
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)
    }
}