package com.kien.petclub.domain.usecase.storage

import android.net.Uri
import com.kien.petclub.domain.repository.FirebaseStorageRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(private val storageRepositoryImpl: FirebaseStorageRepository) {
    operator fun invoke(uri: Uri, nameFile: String) =
        storageRepositoryImpl.uploadImage(uri, nameFile)

    operator fun invoke(listImages: List<Uri>?): Flow<Resource<List<String>>> {
        return if (listImages.isNullOrEmpty()) flowOf(Resource.success(listOf()))
        else storageRepositoryImpl.uploadListImage(listImages)
    }


}