package com.kien.petclub.domain.usecase.auth

import com.google.firebase.auth.FirebaseUser
import com.kien.petclub.constants.Constants.EMPTY_EMAIL
import com.kien.petclub.constants.Constants.EMPTY_NAME
import com.kien.petclub.constants.Constants.EMPTY_PASSWORD
import com.kien.petclub.constants.Constants.EMPTY_PHONE
import com.kien.petclub.constants.Constants.INVALID_EMAIL
import com.kien.petclub.constants.Constants.INVALID_PASSWORD
import com.kien.petclub.constants.Constants.PASSWORD_NOT_MATCH
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
                emit(Resource.Failure(IllegalArgumentException(EMPTY_EMAIL)))
            }
        }

        if (!AuthUtils.isValidEmail(email)) {
            return flow {
                emit(Resource.Failure(IllegalArgumentException(INVALID_EMAIL)))
            }
        }

        if (password.isEmpty()) {
            return flow {
                emit(Resource.Failure(IllegalArgumentException(EMPTY_PASSWORD)))
            }
        }

        if (!AuthUtils.isValidPassword(password)) {
            return flow {
                emit(Resource.Failure(IllegalArgumentException(INVALID_PASSWORD)))
            }
        }

        if (password != rePassword) {
            return flow {
                emit(Resource.Failure(IllegalArgumentException(PASSWORD_NOT_MATCH)))
            }
        }

        if (name.isEmpty()) {
            return flow { emit(Resource.Failure(IllegalArgumentException(EMPTY_NAME))) }
        }

        if (phone.isEmpty()) {
            return flow { (Resource.Failure(IllegalArgumentException(EMPTY_PHONE))) }
        }

        return repo.signUp(email, password)
    }
}