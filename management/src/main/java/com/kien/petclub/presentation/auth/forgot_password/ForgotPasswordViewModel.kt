package com.kien.petclub.presentation.auth.forgot_password

import androidx.lifecycle.viewModelScope
import com.kien.petclub.domain.usecase.auth.RecoverPasswordUseCase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val useCase: RecoverPasswordUseCase
) : BaseViewModel() {
    private val _recoverPasswordResponse = MutableStateFlow<Resource<Unit>>(Resource.Default)
    val recoverPasswordResponse = _recoverPasswordResponse.asStateFlow()

    fun recoverPassword(email: String) {
        viewModelScope.launch {
            useCase(email).collect {
                _recoverPasswordResponse.value = it
            }
        }
    }
}