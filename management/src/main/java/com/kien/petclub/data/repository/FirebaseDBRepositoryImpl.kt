package com.kien.petclub.data.repository

import com.google.firebase.database.FirebaseDatabase
import com.kien.petclub.constants.Constants
import com.kien.petclub.domain.model.entity.User
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseDBRepositoryImpl @Inject constructor(
    db: FirebaseDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : FirebaseDBRepository {
    private val userDatabase = db.reference.child(Constants.USER_DB)
    override fun addUserDatabase(
        userId: String,
        email: String,
        name: String,
        phoneNumber: String
    ): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val user = User(userId, email, name, phoneNumber)
            userDatabase.child(userId).setValue(user).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun updateUserDatabase(userId: String, user: User): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            userDatabase.child(userId).setValue(user).await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun getUserDatabase(userId: String): Flow<Resource<User?>> = flow {
        emit(Resource.Loading)
        val snapshot = userDatabase.child(userId).get().await()
        try {
            emit(Resource.success(snapshot.getValue(User::class.java)))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

    override fun deleteUserDatabase(userId: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            userDatabase.child(userId).removeValue().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(dispatcher)

}