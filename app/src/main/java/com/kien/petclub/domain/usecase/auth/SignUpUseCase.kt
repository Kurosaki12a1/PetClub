package com.kien.petclub.domain.usecase.auth

import com.google.firebase.auth.FirebaseUser
import com.kien.petclub.domain.repository.AuthRepository
import com.kien.petclub.domain.util.AuthUtils
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repo: AuthRepository) {
    operator fun invoke(
        email: String,
        password: String,
        rePassword: String,
        name: String,
        phone: String
    ): Flow<Resource<FirebaseUser?>> {
        if (email.isEmpty()) {
            return flow {
                emit(Resource.Failure(IllegalArgumentException("Email is empty")))
            }
        }

        if (!AuthUtils.isValidEmail(email)) {
            return flow {
                emit(Resource.Failure(IllegalArgumentException("Email format is invalid.")))
            }
        }

        if (password.isEmpty()) {
            return flow {
                emit(Resource.Failure(IllegalArgumentException("Password is empty")))
            }
        }

        if (!AuthUtils.isValidPassword(password)) {
            return flow {
                emit(Resource.Failure(IllegalArgumentException("Password must be at least 6 characters long and contain at least one uppercase letter")))
            }
        }

        if (password != rePassword) {
            return flow {
                emit(Resource.Failure(IllegalArgumentException("Password and re-password are not the same")))
            }
        }

        if (name.isEmpty()) {
            return flow { emit(Resource.Failure(IllegalArgumentException("Name is empty"))) }
        }

        if (phone.isEmpty()) {
            return flow {(Resource.Failure(IllegalArgumentException("Phone is empty")))}
        }

        return repo.signUp(email, password)
    }
}