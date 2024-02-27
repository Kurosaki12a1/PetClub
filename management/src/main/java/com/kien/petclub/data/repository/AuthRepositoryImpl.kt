package com.kien.petclub.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.kien.petclub.domain.repository.AuthRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AuthRepository {
    override fun signIn(email: String, password: String): Flow<Resource<FirebaseUser?>> =
        flow<Resource<FirebaseUser?>> {
            emit(Resource.Loading)
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val user = result.user
                if (user != null) {
                    emit(Resource.success(user))
                } else {
                    emit(Resource.failure(Exception("User is not exist")))
                }
            } catch (e: Exception) {
                emit(Resource.failure(e))
            }
        }.flowOn(dispatcher)

    override fun signOut(): Flow<Resource<Unit>> = flow {
        try {
            auth.signOut()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun recoverPassword(email: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            auth.sendPasswordResetEmail(email).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun signUp(email: String, password: String): Flow<Resource<FirebaseUser?>> = flow {
        emit(Resource.Loading)
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            emit(Resource.success(result.user))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun isSignedIn(): Flow<Boolean> = flow {
        val user = auth.currentUser
        if (user != null) {
            emit(true)
        } else {
            emit(false)
        }
    }.flowOn(dispatcher)

}