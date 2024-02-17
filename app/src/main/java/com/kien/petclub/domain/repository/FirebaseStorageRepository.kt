package com.kien.petclub.domain.repository

import android.net.Uri
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseStorageRepository {
    fun uploadImage(uri: Uri, nameFile: String): Flow<Resource<String>>

    fun uploadListImage(listImages: List<Uri>): Flow<Resource<List<String>>>

    fun downloadImage(fileRef: String): Flow<Resource<Uri>>

    fun downloadListImage(listFileRef: List<String>): Flow<Resource<List<Uri>>>

    fun deleteImage(fileRef: String): Flow<Resource<Unit>>
}