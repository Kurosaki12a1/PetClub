package com.kien.petclub.domain.repository

interface AuthRepository {

    fun signIn(email: String, password: String)

    fun signOut()
}