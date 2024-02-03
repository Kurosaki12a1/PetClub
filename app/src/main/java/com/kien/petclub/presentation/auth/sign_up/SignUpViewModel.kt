package com.kien.petclub.presentation.auth.sign_up

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.kien.petclub.domain.usecase.auth.SignUpUseCase
import com.kien.petclub.domain.usecase.firebase_db.AddUserUseCase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val addUserUseCase: AddUserUseCase
) : BaseViewModel() {

    private val _signUpResponse = MutableStateFlow(false)
    val signUpResponse = _signUpResponse

    private val _error = MutableStateFlow<String?>("")
    val error = _error

    fun signUp(email: String, password: String, rePassword: String, name: String, phone: String) {
        viewModelScope.launch {
            signUpUseCase(email, password, rePassword, name, phone)
                .flatMapConcat {
                    when (it) {
                        is Resource.Success -> {
                            addUserUseCase(it.value!!.uid, name, phone)
                        }

                        is Resource.Failure -> {
                            flow { emit(Resource.failure(it.error, it.errorMessage)) }
                        }
                    }
                }.collect {
                    when (it) {
                        is Resource.Success -> {
                            _signUpResponse.value = true
                        }
                        is Resource.Failure -> {
                            _signUpResponse.value = false
                            _error.value = it.error.message.toString()
                        }
                    }
                }
        }
    }
}