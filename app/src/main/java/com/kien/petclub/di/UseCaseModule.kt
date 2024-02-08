package com.kien.petclub.di

import com.google.firebase.storage.FirebaseStorage
import com.kien.petclub.domain.repository.AuthRepository
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.repository.FirebaseStorageRepository
import com.kien.petclub.domain.usecase.auth.SignInUseCase
import com.kien.petclub.domain.usecase.auth.SignOutUseCase
import com.kien.petclub.domain.usecase.auth.SignUpUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.AddInfoProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.AddProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.CheckExistenceProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.DeleteProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.GetGoodsUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.GetInfoProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.GetProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.GetServiceUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.SearchInfoProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.UpdateProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.user.AddUserUseCase
import com.kien.petclub.domain.usecase.firebase_db.user.DeleteUserUseCase
import com.kien.petclub.domain.usecase.firebase_db.user.GetUserUseCase
import com.kien.petclub.domain.usecase.firebase_db.user.UpdateUserUseCase
import com.kien.petclub.domain.usecase.storage.UploadImageUseCase
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
    fun provideGetGoodsUseCase(firebaseDBRepository: FirebaseDBRepository): GetGoodsUseCase {
        return GetGoodsUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateProductUseCase(firebaseDBRepository: FirebaseDBRepository): UpdateProductUseCase {
        return UpdateProductUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteProductUseCase(firebaseDBRepository: FirebaseDBRepository): DeleteProductUseCase {
        return DeleteProductUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideAddProductUseCase(firebaseDBRepository: FirebaseDBRepository): AddProductUseCase {
        return AddProductUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideGetServiceUseCase(firebaseDBRepository: FirebaseDBRepository): GetServiceUseCase {
        return GetServiceUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideGetProductUseCase(
        goodsUseCase: GetGoodsUseCase,
        serviceUseCase: GetServiceUseCase
    ): GetProductUseCase {
        return GetProductUseCase(goodsUseCase, serviceUseCase)
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

    @Provides
    @Singleton
    fun provideCheckExistUserUseCase(firebaseDBRepository: FirebaseDBRepository): CheckExistenceProductUseCase {
        return CheckExistenceProductUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideUploadImageUseCase(storage : FirebaseStorageRepository) : UploadImageUseCase {
        return UploadImageUseCase(storage)
    }

    @Provides
    @Singleton
    fun provideAddInfoProductUseCase(firebaseDBRepository: FirebaseDBRepository): AddInfoProductUseCase {
        return AddInfoProductUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideSearchInfoProductUseCase(firebaseDBRepository: FirebaseDBRepository): SearchInfoProductUseCase {
        return SearchInfoProductUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideGetInfoProductUseCase(firebaseDBRepository: FirebaseDBRepository): GetInfoProductUseCase {
        return GetInfoProductUseCase(firebaseDBRepository)
    }

}