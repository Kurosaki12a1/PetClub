package com.kien.main.di

import android.content.ContentProvider
import android.content.ContentResolver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kien.imagepicker.repository.ImagePickerRepository
import com.kien.petclub.data.repository.AuthRepositoryImpl
import com.kien.petclub.data.repository.FirebaseDBRepositoryImpl
import com.kien.petclub.data.repository.FirebaseStorageRepositoryImpl
import com.kien.petclub.domain.repository.AuthRepository
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.repository.FirebaseStorageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
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

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository = AuthRepositoryImpl(auth, Dispatchers.IO)

    @Provides
    @Singleton
    fun provideFirebaseStorageRepository(
        storage: FirebaseStorage
    ): FirebaseStorageRepository =
        FirebaseStorageRepositoryImpl(storage)

    @Provides
    @Singleton
    fun provideImagePickerRepository(
        contentResolver: ContentResolver
    ): ImagePickerRepository = ImagePickerRepository(contentResolver)
}