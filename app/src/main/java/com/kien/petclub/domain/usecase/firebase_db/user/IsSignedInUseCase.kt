package com.kien.petclub.domain.usecase.firebase_db.user

import com.kien.petclub.domain.repository.AuthRepository
import javax.inject.Inject

class IsSignedInUseCase @Inject constructor(private val auth: AuthRepository) {
    operator fun invoke() = auth.isSignedIn()
}