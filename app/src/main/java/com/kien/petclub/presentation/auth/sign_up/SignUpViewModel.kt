package com.kien.petclub.presentation.auth.sign_up

import androidx.lifecycle.viewModelScope
import com.kien.petclub.domain.usecase.auth.SignUpUseCase
import com.kien.petclub.domain.usecase.firebase_db.user.AddUserUseCase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val addUserUseCase: AddUserUseCase
) : BaseViewModel() {

    private val _signUpResponse = MutableStateFlow<Resource<Unit>>(Resource.Default)
    val signUpResponse = _signUpResponse.asStateFlow()

    fun signUp(email: String, password: String, rePassword: String, name: String, phone: String) {
        viewModelScope.launch {
            signUpUseCase(email, password, rePassword, name, phone)
                .flatMapConcat {
                    when (it) {
                        is Resource.Success -> {
                            addUserUseCase(it.value!!.uid, email, name, phone)
                        }
                        is Resource.Failure -> {
                            flow { emit(Resource.failure(it.error, it.errorMessage)) }
                        }
                        is Resource.Loading -> {
                            flow { emit(Resource.Loading) }
                        }
                        else -> {
                            flow { emit(Resource.Default) }
                        }
                    }
                }.collect {
                    _signUpResponse.value = it
                }
        }
    }
}