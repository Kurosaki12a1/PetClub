package com.kien.petclub.presentation.auth.sign_up

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kien.petclub.databinding.FragmentSignUpBinding
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.extensions.openActivityAndClearStack
import com.kien.petclub.presentation.base.BaseFragment
import com.kien.petclub.presentation.home.HomeActivity
import com.kien.petclub.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
        lifecycleScope.launch {  }
        viewModel.signUpResponse.onEach {
            if (it) {
                backToPreviousScreen()
            }
        }.launchIn(lifecycleScope)
        viewModel.error.onEach {
            if (it?.isNotEmpty() == true) {
                showMessage(requireActivity(), it)
            }
        }.launchIn(lifecycleScope)
    }

}