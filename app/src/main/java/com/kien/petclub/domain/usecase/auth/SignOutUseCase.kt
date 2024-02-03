package com.kien.petclub.domain.usecase.auth

import com.kien.petclub.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(val auth : AuthRepository) {
    operator fun invoke() = auth.signOut()
}