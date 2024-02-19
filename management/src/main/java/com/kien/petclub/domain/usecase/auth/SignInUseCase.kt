package com.kien.petclub.domain.usecase.auth

import com.google.firebase.auth.FirebaseUser
import com.kien.petclub.domain.repository.AuthRepository
import com.kien.petclub.domain.util.AuthUtils
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(val auth: AuthRepository) {
    operator fun invoke(email: String, password: String): Flow<Resource<FirebaseUser?>> {
        if (!AuthUtils.isValidEmail(email) || !AuthUtils.isValidPassword(password)) {
            return flow { emit(Resource.failure(IllegalArgumentException("Email or password format is invalid."))) }
        }
        return auth.signIn(email, password)
    }
}