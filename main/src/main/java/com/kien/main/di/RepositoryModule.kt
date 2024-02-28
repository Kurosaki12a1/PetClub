package com.kien.main.di

import android.content.ContentResolver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.kien.imagepicker.data.repository.ImagePickerRepositoryImpl
import com.kien.imagepicker.domain.repository.ImagePickerRepository
import com.kien.petclub.data.repository.AuthRepositoryImpl
import com.kien.petclub.data.repository.FirebaseDBRepositoryImpl
import com.kien.petclub.data.repository.FirebaseStorageRepositoryImpl
import com.kien.petclub.data.repository.GoodsRepositoryImpl
import com.kien.petclub.data.repository.InfoProductRepositoryImpl
import com.kien.petclub.data.repository.ServiceRepositoryImpl
import com.kien.petclub.domain.repository.AuthRepository
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.repository.FirebaseStorageRepository
import com.kien.petclub.domain.repository.GoodsRepository
import com.kien.petclub.domain.repository.InfoProductRepository
import com.kien.petclub.domain.repository.ServiceRepository
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
        db: FirebaseDatabase
    ): FirebaseDBRepository =
        FirebaseDBRepositoryImpl(db, Dispatchers.IO)

    @Provides
    @Singleton
    fun provideGoodsRepository(
        auth: FirebaseAuth,
        db: FirebaseDatabase
    ): GoodsRepository = GoodsRepositoryImpl(auth, db, Dispatchers.IO)

    @Provides
    @Singleton
    fun provideServiceRepository(
        auth: FirebaseAuth,
        db: FirebaseDatabase
    ): ServiceRepository = ServiceRepositoryImpl(auth, db, Dispatchers.IO)

    @Provides
    @Singleton
    fun provideInfoProductRepository(
        db: FirebaseDatabase
    ): InfoProductRepository = InfoProductRepositoryImpl(db, Dispatchers.IO)

    @Provides
    @Singleton
    fun provideAuthRepository(auth: FirebaseAuth): AuthRepository =
        AuthRepositoryImpl(auth, Dispatchers.IO)

    @Provides
    @Singleton
    fun provideFirebaseStorageRepository(
        storage: FirebaseStorage
    ): FirebaseStorageRepository =
        FirebaseStorageRepositoryImpl(storage, Dispatchers.IO)

    @Provides
    @Singleton
    fun provideImagePickerRepository(
        contentResolver: ContentResolver
    ): ImagePickerRepository = ImagePickerRepositoryImpl(contentResolver)
}