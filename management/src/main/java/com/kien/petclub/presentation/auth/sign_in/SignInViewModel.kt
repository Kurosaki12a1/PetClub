package com.kien.petclub.presentation.auth.sign_in

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.kien.petclub.domain.usecase.auth.SignInUseCase
import com.kien.petclub.domain.usecase.firebase_db.user.IsSignedInUseCase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val isSignedInUseCase: IsSignedInUseCase
) : BaseViewModel() {

    private val _isSignedInResponse = MutableStateFlow(false)
    val isSignedInResponse = _isSignedInResponse.asStateFlow()

    private val _signInResponse = MutableStateFlow<Resource<FirebaseUser?>>(Resource.Default)
    val signInResponse = _signInResponse.asStateFlow()

    init {
        checkIsSignedIn()
    }

    private fun checkIsSignedIn() {
        viewModelScope.launch {
            isSignedInUseCase.invoke().collect {
                _isSignedInResponse.value = it
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            signInUseCase(email, password).collect {
                _signInResponse.value = it
            }
        }
    }
}