package com.kien.petclub.domain.usecase.auth

import com.kien.petclub.domain.repository.AuthRepository
import javax.inject.Inject

class RecoverPasswordUseCase @Inject constructor(val auth : AuthRepository) {
    operator fun invoke(email: String) = auth.recoverPassword(email)
}