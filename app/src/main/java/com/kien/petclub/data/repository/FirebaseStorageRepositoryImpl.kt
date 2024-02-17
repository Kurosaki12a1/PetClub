package com.kien.petclub.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.kien.petclub.domain.repository.FirebaseStorageRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageRepositoryImpl @Inject constructor(private val storage: FirebaseStorage) :
    FirebaseStorageRepository {
    override fun uploadImage(uri: Uri, nameFile: String): Flow<Resource<String>> = flow {
        emit(Resource.Loading)
        try {
            val ref = storage.reference.child("images/$nameFile.jpg")
            // Start upload
            ref.putFile(uri)

            // After upload, get download url
            val downloadUri = ref.downloadUrl.await().toString()
            emit(Resource.success(downloadUri))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun downloadImage(fileRef: String): Flow<Resource<Uri>> = flow {
        emit(Resource.Loading)
        try {
            val ref = storage.reference.child(fileRef)
            val downloadUri = ref.downloadUrl.await()
            emit(Resource.success(downloadUri))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun deleteImage(fileRef: String): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading)
        try {
            val ref = storage.reference.child(fileRef)
            ref.delete().await()
            emit(Resource.success(Unit))
        } catch (e: Exception) {
            emit(Resource.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun uploadListImage(listImages: List<Uri>): Flow<Resource<List<String>>> = flow {
        emit(Resource.Loading)
        val imageUrls = mutableListOf<String>()
        val timeStamp = System.currentTimeMillis()
        listImages.forEachIndexed { index, uri ->
            val nameFile = "img_${timeStamp}_$index"
            val imageRef = storage.reference.child("images/$nameFile.jpg")
            val uploadTask = imageRef.putFile(uri).await()
            val imageUrl = uploadTask.storage.downloadUrl.await().toString()
            imageUrls.add(imageUrl)
        }
        emit(Resource.success(imageUrls))
    }.catch { error -> emit(Resource.failure(error)) }.flowOn(Dispatchers.IO)

    override fun downloadListImage(listFileRef: List<String>): Flow<Resource<List<Uri>>> = flow {
        emit(Resource.Loading)
        val imageUris = mutableListOf<Uri>()
        listFileRef.forEach { fileRef ->
            val ref = storage.getReferenceFromUrl(fileRef)
            val downloadUri = ref.downloadUrl.await()
            imageUris.add(downloadUri)
        }
        emit(Resource.success(imageUris))
    }.catch { error -> emit(Resource.failure(error)) }.flowOn(Dispatchers.IO)

}