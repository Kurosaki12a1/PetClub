package com.kien.main.di

import com.kien.imagepicker.domain.repository.ImagePickerRepository
import com.kien.imagepicker.domain.usecase.GetAlbumsUseCase
import com.kien.imagepicker.domain.usecase.GetPhotosUseCase
import com.kien.petclub.domain.repository.AuthRepository
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.repository.FirebaseStorageRepository
import com.kien.petclub.domain.repository.GoodsRepository
import com.kien.petclub.domain.repository.InfoProductRepository
import com.kien.petclub.domain.repository.ServiceRepository
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
    fun provideGetGoodsUseCase(goodsRepository: GoodsRepository): GetGoodsUseCase {
        return GetGoodsUseCase(goodsRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateProductUseCase(
        goodsRepository: GoodsRepository,
        serviceRepository: ServiceRepository
    ): UpdateProductUseCase {
        return UpdateProductUseCase(goodsRepository, serviceRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteProductUseCase(
        goodsRepository: GoodsRepository,
        serviceRepository: ServiceRepository
    ): DeleteProductUseCase {
        return DeleteProductUseCase(goodsRepository, serviceRepository)
    }

    @Provides
    @Singleton
    fun provideAddProductUseCase(
        goodsRepository: GoodsRepository,
        serviceRepository: ServiceRepository
    ): AddProductUseCase {
        return AddProductUseCase(goodsRepository, serviceRepository)
    }

    @Provides
    @Singleton
    fun provideGetServiceUseCase(serviceRepository: ServiceRepository): GetServiceUseCase {
        return GetServiceUseCase(serviceRepository)
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
    fun provideCheckExistProductUseCase(
        goodsRepository: GoodsRepository,
        serviceRepository: ServiceRepository
    ): CheckExistenceProductUseCase {
        return CheckExistenceProductUseCase(serviceRepository, goodsRepository)
    }

    @Provides
    @Singleton
    fun provideUploadImageUseCase(storage: FirebaseStorageRepository): UploadImageUseCase {
        return UploadImageUseCase(storage)
    }

    @Provides
    @Singleton
    fun provideAddInfoProductUseCase(infoProductRepository: InfoProductRepository): AddInfoProductUseCase {
        return AddInfoProductUseCase(infoProductRepository)
    }

    @Provides
    @Singleton
    fun provideSearchInfoProductUseCase(infoProductRepository: InfoProductRepository): SearchInfoProductUseCase {
        return SearchInfoProductUseCase(infoProductRepository)
    }

    @Provides
    @Singleton
    fun provideGetInfoProductUseCase(infoProductRepository: InfoProductRepository): GetInfoProductUseCase {
        return GetInfoProductUseCase(infoProductRepository)
    }

    @Provides
    @Singleton
    fun provideDeleteInfoProductUseCase(infoProductRepository: InfoProductRepository): DeleteInfoProductUseCase {
        return DeleteInfoProductUseCase(infoProductRepository)
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

    @Provides
    @Singleton
    fun provideGetAlbumsUseCase(repo: ImagePickerRepository): GetAlbumsUseCase {
        return GetAlbumsUseCase(repo)
    }

    @Provides
    @Singleton
    fun provideGetPhotosUseCase(repo: ImagePickerRepository): GetPhotosUseCase {
        return GetPhotosUseCase(repo)
    }

}