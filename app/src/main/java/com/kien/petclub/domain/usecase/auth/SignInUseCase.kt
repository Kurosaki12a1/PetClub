package com.kien.petclub.domain.usecase.auth

import com.kien.petclub.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(val auth : AuthRepository) {
    operator fun invoke(email: String, password: String) = auth.signIn(email, password)
}