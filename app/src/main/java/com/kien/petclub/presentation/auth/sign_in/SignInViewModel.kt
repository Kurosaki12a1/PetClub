package com.kien.petclub.presentation.auth.sign_in

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.kien.petclub.domain.usecase.auth.SignInUseCase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : BaseViewModel() {

    private val _signInResponse = MutableStateFlow<Resource<FirebaseUser?>>(Resource.Default)
    val signInResponse = _signInResponse.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase(email, password).collect {
                _signInResponse.value = it
            }
        }
    }
}