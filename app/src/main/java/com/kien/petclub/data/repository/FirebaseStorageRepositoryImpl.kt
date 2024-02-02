package com.kien.petclub.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.kien.petclub.domain.repository.FirebaseStorageRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageRepositoryImpl @Inject constructor(private val storage: FirebaseStorage) :
    FirebaseStorageRepository {
    override fun uploadImage(uri: Uri, nameFile: String): Flow<Resource<Uri>> = flow {
        try {
            val ref = storage.reference.child("images/$nameFile.jpg")
            // Start upload
            ref.putFile(uri)

            // After upload, get download url
            val downloadUri = ref.downloadUrl.await()
            emit(Resource.success(downloadUri))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun downloadImage(fileRef: String): Flow<Resource<Uri>> = flow {
        try {
            val ref = storage.reference.child(fileRef)
            val downloadUri = ref.downloadUrl.await()
            emit(Resource.success(downloadUri))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteImage(fileRef: String): Flow<Resource<Unit>> = flow {
        try {
            val ref = storage.reference.child(fileRef)
            ref.delete().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

}