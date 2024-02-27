package com.kien.petclub.presentation.auth.sign_in

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kien.petclub.databinding.FragmentSignInBinding
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.navigateSafe
import com.kien.petclub.extensions.showMessage
import com.kien.petclub.presentation.auth.AuthActivity
import com.kien.petclub.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>() {

    private val viewModel: SignInViewModel by viewModels()

    override fun getViewBinding(): FragmentSignInBinding =
        FragmentSignInBinding.inflate(layoutInflater)


    override fun setUpViews() {
        super.setUpViews()

        binding.ivLogin.setOnClickListener { signIn() }

        binding.tvForgotPassword.setOnClickListener { openForgotPassword() }

        binding.ivRegister.setOnClickListener { openSignUp() }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.signInResponse.onEach {
            when (it) {
                is Resource.Success -> {
                    (requireActivity() as AuthActivity).apply {
                        stopLoadingAnimation()
                        openHome()
                    }
                }

                is Resource.Loading -> {
                    (requireActivity() as AuthActivity).showLoadingAnimation()
                }

                is Resource.Failure -> {
                    (requireActivity() as AuthActivity).stopLoadingAnimation()
                    showMessage(it.error.message.toString())
                }

                else -> {}
            }
        }.launchIn(lifecycleScope)

        lifecycleScope.launch {
            viewModel.isSignedInResponse.collectLatest {
                if (it) {
                    (requireActivity() as AuthActivity).openHome()
                }
            }
        }
    }


    private fun openForgotPassword() {
        navigateSafe(SignInFragmentDirections.actionOpenForgotPasswordFragment())
    }

    private fun openSignUp() {
        navigateSafe(SignInFragmentDirections.actionOpenSignUpFragment())
    }

    private fun signIn() {
        viewModel.signIn(
            binding.etEmail.text.toString(),
            binding.etPassword.text.toString()
        )
    }

}