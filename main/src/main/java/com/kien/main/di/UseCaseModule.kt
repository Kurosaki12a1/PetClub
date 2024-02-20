package com.kien.main.di

import com.kien.petclub.domain.repository.AuthRepository
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.repository.FirebaseStorageRepository
import com.kien.petclub.domain.usecase.auth.SignInUseCase
import com.kien.petclub.domain.usecase.auth.SignOutUseCase
import com.kien.petclub.domain.usecase.auth.SignUpUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.AddProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.CheckExistenceProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.DeleteProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.GetGoodsUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.GetProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.GetServiceUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.ProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.UpdateProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.info_product.AddInfoProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.info_product.DeleteInfoProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.info_product.GetInfoProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.info_product.InfoProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.info_product.SearchInfoProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.user.AddUserUseCase
import com.kien.petclub.domain.usecase.firebase_db.user.DeleteUserUseCase
import com.kien.petclub.domain.usecase.firebase_db.user.GetUserUseCase
import com.kien.petclub.domain.usecase.firebase_db.user.IsSignedInUseCase
import com.kien.petclub.domain.usecase.firebase_db.user.UpdateUserUseCase
import com.kien.petclub.domain.usecase.storage.DownloadImageUseCase
import com.kien.petclub.domain.usecase.storage.ImageUseCase
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
    fun provideIsSignedInUseCase(authRepository: AuthRepository): IsSignedInUseCase {
        return IsSignedInUseCase(authRepository)
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
    fun provideUploadImageUseCase(storage: FirebaseStorageRepository): UploadImageUseCase {
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

    @Provides
    @Singleton
    fun provideDeleteInfoProductUseCase(firebaseDBRepository: FirebaseDBRepository): DeleteInfoProductUseCase {
        return DeleteInfoProductUseCase(firebaseDBRepository)
    }

    @Provides
    @Singleton
    fun provideDownloadImageUseCase(storage: FirebaseStorageRepository): DownloadImageUseCase {
        return DownloadImageUseCase(storage)
    }

    @Provides
    @Singleton
    fun provideInfoProductUseCase(
        addUseCase: AddInfoProductUseCase,
        deleteUseCase: DeleteInfoProductUseCase,
        getUseCase: GetInfoProductUseCase,
        searchUseCase: SearchInfoProductUseCase
    ): InfoProductUseCase {
        return InfoProductUseCase(addUseCase, deleteUseCase, getUseCase, searchUseCase)
    }

    @Provides
    @Singleton
    fun provideProductUseCase(
        addUseCase: AddProductUseCase,
        deleteUseCase: DeleteProductUseCase,
        getUseCase: GetProductUseCase,
        updateUseCase: UpdateProductUseCase,
        checkExistenceProductUseCase: CheckExistenceProductUseCase
    ): ProductUseCase {
        return ProductUseCase(
            addUseCase,
            deleteUseCase,
            getUseCase,
            updateUseCase,
            checkExistenceProductUseCase
        )
    }

    @Provides
    @Singleton
    fun provideImageUseCase(
        uploadUseCase: UploadImageUseCase,
        downloadUseCase: DownloadImageUseCase
    ): ImageUseCase {
        return ImageUseCase(uploadUseCase, downloadUseCase)
    }

}