package com.kien.petclub.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.kien.petclub.data.repository.FirebaseDBRepositoryImpl
import com.kien.petclub.domain.repository.FirebaseDBRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideFirebaseDBRepository(
        auth: FirebaseAuth,
        db: FirebaseDatabase
    ): FirebaseDBRepository =
        FirebaseDBRepositoryImpl(auth, db)
}