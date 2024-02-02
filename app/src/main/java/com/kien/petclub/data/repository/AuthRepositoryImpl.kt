package com.kien.petclub.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kien.petclub.domain.repository.AuthRepository
import com.kien.petclub.domain.util.AuthUtils
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {
    override fun signIn(email: String, password: String): Flow<Resource<FirebaseUser?>> =
        flow<Resource<FirebaseUser?>> {
            if (!AuthUtils.isValidEmail(email) || !AuthUtils.isValidPassword(password)) {
                emit(Resource.failure(IllegalArgumentException("Email or password format is invalid.")))
                return@flow
            }

            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val user = result.user
                if (user != null && user.isEmailVerified) {
                    emit(Resource.success(user))
                } else {
                    emit(Resource.failure(IllegalArgumentException("Email is not verified.")))
                }
            } catch (e: Exception) {
                emit(Resource.failure(e))
            }
        }.flowOn(Dispatchers.IO)

    override fun signOut(): Flow<Resource<Unit>> = flow {
        try {
            auth.signOut()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun register(email: String, password: String): Flow<Resource<FirebaseUser?>> = flow {
        if (!AuthUtils.isValidEmail(email) || !AuthUtils.isValidPassword(password)) {
            emit(Resource.failure(IllegalArgumentException("Email or password format is invalid.")))
            return@flow
        }

        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.sendEmailVerification()?.await()
            emit(Resource.success(result.user))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

}