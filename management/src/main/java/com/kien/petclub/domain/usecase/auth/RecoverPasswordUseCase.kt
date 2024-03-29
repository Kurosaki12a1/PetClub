package com.kien.petclub.domain.usecase.auth

import com.kien.petclub.constants.Constants.EMPTY_EMAIL
import com.kien.petclub.constants.Constants.INVALID_EMAIL
import com.kien.petclub.domain.repository.AuthRepository
import com.kien.petclub.domain.util.AuthUtils
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecoverPasswordUseCase @Inject constructor(val auth : AuthRepository) {
    operator fun invoke(email: String) : Flow<Resource<Unit>> {
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

        return auth.recoverPassword(email)
    }
}