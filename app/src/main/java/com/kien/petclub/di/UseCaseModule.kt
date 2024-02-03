package com.kien.petclub.di

import com.kien.petclub.domain.repository.AuthRepository
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.usecase.auth.SignInUseCase
import com.kien.petclub.domain.usecase.auth.SignOutUseCase
import com.kien.petclub.domain.usecase.auth.SignUpUseCase
import com.kien.petclub.domain.usecase.firebase_db.AddGoodsUseCase
import com.kien.petclub.domain.usecase.firebase_db.AddServiceUseCase
import com.kien.petclub.domain.usecase.firebase_db.AddUserUseCase
import com.kien.petclub.domain.usecase.firebase_db.DeleteGoodsUseCase
import com.kien.petclub.domain.usecase.firebase_db.DeleteServiceUseCase
import com.kien.petclub.domain.usecase.firebase_db.DeleteUserUseCase
import com.kien.petclub.domain.usecase.firebase_db.GetGoodsUseCase
import com.kien.petclub.domain.usecase.firebase_db.GetServiceUseCase
import com.kien.petclub.domain.usecase.firebase_db.GetUserUseCase
import com.kien.petclub.domain.usecase.firebase_db.UpdateGoodsUseCase
import com.kien.petclub.domain.usecase.firebase_db.UpdateServiceUseCase
import com.kien.petclub.domain.usecase.firebase_db.UpdateUserUseCase
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


    @Provides
    @Singleton
    fun provideAddUserUseCase(firebaseDBRepository: FirebaseDBRepository): AddUserUseCase {
        return AddUserUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteUserUseCase(firebaseDBRepository: FirebaseDBRepository): DeleteUserUseCase {
        return DeleteUserUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserUseCase(firebaseDBRepository: FirebaseDBRepository): GetUserUseCase {
        return GetUserUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateUserUseCase(firebaseDBRepository: FirebaseDBRepository): UpdateUserUseCase {
        return UpdateUserUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideSignInUseCase(authRepository: AuthRepository): SignInUseCase {
        return SignInUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideSignOutUseCase(authRepository: AuthRepository): SignOutUseCase {
        return SignOutUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideSignUpUseCase(authRepository: AuthRepository): SignUpUseCase {
        return SignUpUseCase(authRepository)
    }

}