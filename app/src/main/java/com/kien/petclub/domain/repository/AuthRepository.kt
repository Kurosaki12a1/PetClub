package com.kien.petclub.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun signIn(email: String, password: String) : Flow<Resource<FirebaseUser?>>

    fun signOut() : Flow<Resource<Unit>>

    fun signUp(email: String, password: String) : Flow<Resource<FirebaseUser?>>

    fun recoverPassword(email: String) : Flow<Resource<Unit>>
}