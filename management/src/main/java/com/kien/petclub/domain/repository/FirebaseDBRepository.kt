package com.kien.petclub.domain.repository

import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.model.entity.User
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseDBRepository {

    fun addUserDatabase(
        userId: String,
        email: String,
        name: String,
        phoneNumber: String
    ): Flow<Resource<Unit>>

    fun updateUserDatabase(userId: String, user: User): Flow<Resource<Unit>>

    fun getUserDatabase(userId: String): Flow<Resource<User?>>

    fun deleteUserDatabase(userId: String): Flow<Resource<Unit>>
}