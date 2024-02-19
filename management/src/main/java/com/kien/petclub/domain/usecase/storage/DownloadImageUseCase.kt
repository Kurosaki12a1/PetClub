package com.kien.petclub.domain.usecase.storage

import com.kien.petclub.domain.repository.FirebaseStorageRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class DownloadImageUseCase @Inject constructor(val repo: FirebaseStorageRepository) {
    operator fun invoke(fileRef: String) = repo.downloadImage(fileRef)

    operator fun invoke(listFileRef: List<String>?) =
        if (listFileRef.isNullOrEmpty()) flowOf(Resource.success(listOf()))
        else repo.downloadListImage(listFileRef)
}