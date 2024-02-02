package com.kien.petclub.di

import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.usecase.firebase_db.AddGoodsUseCase
import com.kien.petclub.domain.usecase.firebase_db.AddServiceUseCase
import com.kien.petclub.domain.usecase.firebase_db.DeleteGoodsUseCase
import com.kien.petclub.domain.usecase.firebase_db.DeleteServiceUseCase
import com.kien.petclub.domain.usecase.firebase_db.GetGoodsUseCase
import com.kien.petclub.domain.usecase.firebase_db.GetServiceUseCase
import com.kien.petclub.domain.usecase.firebase_db.UpdateGoodsUseCase
import com.kien.petclub.domain.usecase.firebase_db.UpdateServiceUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideAddGoodsUseCase(firebaseDBRepository: FirebaseDBRepository): AddGoodsUseCase {
        return AddGoodsUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideGetGoodsUseCase(firebaseDBRepository: FirebaseDBRepository): GetGoodsUseCase {
        return GetGoodsUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateGoodsUseCase(firebaseDBRepository: FirebaseDBRepository): UpdateGoodsUseCase {
        return UpdateGoodsUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteGoodsUseCase(firebaseDBRepository: FirebaseDBRepository): DeleteGoodsUseCase {
        return DeleteGoodsUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideAddServiceUseCase(firebaseDBRepository: FirebaseDBRepository): AddServiceUseCase {
        return AddServiceUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideGetServiceUseCase(firebaseDBRepository: FirebaseDBRepository): GetServiceUseCase {
        return GetServiceUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateServiceUseCase(firebaseDBRepository: FirebaseDBRepository): UpdateServiceUseCase {
        return UpdateServiceUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteServiceUseCase(firebaseDBRepository: FirebaseDBRepository): DeleteServiceUseCase {
        return DeleteServiceUseCase(firebaseDBRepository)
    }

}