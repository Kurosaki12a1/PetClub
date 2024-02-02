package com.kien.petclub.domain.repository

import android.net.Uri
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseStorageRepository {
    fun uploadImage(uri: Uri, nameFile : String) : Flow<Resource<Uri>>
    fun downloadImage(fileRef: String) : Flow<Resource<Uri>>
    fun deleteImage(fileRef: String) : Flow<Resource<Unit>>
}