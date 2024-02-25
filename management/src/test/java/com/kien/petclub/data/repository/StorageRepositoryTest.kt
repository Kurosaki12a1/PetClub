package com.kien.petclub.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.UploadTask.TaskSnapshot
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.utils.mockTask
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class StorageRepositoryTest {

    private lateinit var storage: FirebaseStorage

    private lateinit var ref: StorageReference

    private lateinit var repo: FirebaseStorageRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        storage = mockk(relaxed = true)
        ref = storage.reference
        repo = FirebaseStorageRepositoryImpl(storage, testDispatcher)
    }

    @Test
    fun `uploadImage with valid uri and nameFile emits success`() = runTest(testDispatcher) {
        val uri = mockk<Uri>()
        val putFile = mockk<UploadTask>()
        val downloadUri = mockTask<Uri>()
        val resultTask = mockk<Uri>()
        val uriToString = ""
        every { ref.child(any()) } returns ref
        every { ref.putFile(uri) } returns putFile
        every { ref.downloadUrl } returns downloadUri
        every { downloadUri.result } returns resultTask
        every { resultTask.toString() } returns uriToString
        val result = repo.uploadImage(uri, "name").toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Success && (result.last() as Resource.Success<String>).value == uriToString)
    }

    @Test
    fun `uploadImage with invalid uri and nameFile emits error`() = runTest(testDispatcher) {
        val uri = mockk<Uri>()
        val putFile = mockk<UploadTask>()
        val downloadUri = mockTask<Uri>()
        val exception = mockk<Exception>()
        every { ref.child(any()) } returns ref
        every { ref.putFile(uri) } returns putFile
        every { ref.downloadUrl } returns downloadUri
        every { downloadUri.exception } returns exception
        val result = repo.uploadImage(uri, "name").toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Failure && (result.last() as Resource.Failure).error == exception)
    }

    @Test
    fun `downloadImage with valid fileRef emits success`() = runTest(testDispatcher) {
        val downloadUri = mockTask<Uri>()
        val resultTask = mockk<Uri>()
        every { ref.child(any()) } returns ref
        every { ref.downloadUrl } returns downloadUri
        every { downloadUri.result } returns resultTask
        val result = repo.downloadImage("fileRef").toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Success && (result.last() as Resource.Success<Uri>).value == resultTask)
    }

    @Test
    fun `downloadImage with invalid fileRef emits error`() = runTest(testDispatcher) {
        val downloadUri = mockTask<Uri>()
        val exception = mockk<Exception>()
        every { ref.child(any()) } returns ref
        every { ref.downloadUrl } returns downloadUri
        every { downloadUri.result } throws exception
        val result = repo.downloadImage("fileRef").toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Failure && (result.last() as Resource.Failure).error == exception)
    }

    @Test
    fun `downloadImage with invalid fileRef emits error 2`() = runTest(testDispatcher) {
        val exception = mockk<Exception>()
        every { ref.child(any()) } returns ref
        every { ref.downloadUrl } throws exception
        val result = repo.downloadImage("fileRef").toList()
        assert(result.first() is Resource.Loading)
        assert(
            result.last() is Resource.Failure && (result.last() as Resource.Failure).error == exception
        )
    }

    @Test
    fun `deleteImage with valid fileRef emits success`() = runTest(testDispatcher) {
        val deleteTask = mockTask<Void>()
        every { ref.child(any()) } returns ref
        every { ref.delete() } returns deleteTask
        val result = repo.deleteImage("fileRef").toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Success && (result.last() as Resource.Success<Unit>).value == Unit)
    }

    @Test
    fun `deleteImage with invalid fileRef emits error`() = runTest(testDispatcher) {
        val exception = mockk<Exception>()
        every { ref.child(any()) } returns ref
        every { ref.delete() } throws exception
        val result = repo.deleteImage("fileRef").toList()
        assert(result.first() is Resource.Loading)
        assert(
            result.last() is Resource.Failure && (result.last() as Resource.Failure).error == exception
        )
    }

    @Test
    fun `uploadListImage with valid listImages emits success`() = runTest(testDispatcher) {
        val uploadList = List(10) { mockk<Uri>() }
        val uploadTask = mockk<UploadTask>()
        val mockTaskSnapShot = mockk<TaskSnapshot>()
        val downloadUri = mockTask<Uri>()
        val resultTask = mockk<Uri>()
        val url = ""
        every { ref.child(any<String>()) } returns ref
        every { ref.putFile(any<Uri>()) } returns uploadTask
        every { uploadTask.isComplete } returns true
        every { uploadTask.exception } returns null
        every { uploadTask.isCanceled } returns false
        every { uploadTask.result } returns mockTaskSnapShot
        every { mockTaskSnapShot.storage.downloadUrl } returns downloadUri
        every { downloadUri.result } returns resultTask
        every { resultTask.toString() } returns url
        val result = repo.uploadListImage(uploadList).toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Success && (result.last() as Resource.Success<List<String>>).value.size == uploadList.size)
    }

    @Test
    fun `uploadListImage with invalid listImages emits error`() = runTest(testDispatcher) {
        val uploadList = List(10) { mockk<Uri>() }
        val uploadTask = mockk<UploadTask>()
        val exception = mockk<Exception>()
        every { ref.child(any<String>()) } returns ref
        every { ref.putFile(any<Uri>()) } returns uploadTask
        every { uploadTask.isComplete } returns true
        every { uploadTask.exception } returns exception
        every { uploadTask.isCanceled } returns false
        val result = repo.uploadListImage(uploadList).toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Failure && (result.last() as Resource.Failure).error == exception)
    }

    @Test
    fun `uploadListImage with invalid listImages emits error 2`() = runTest(testDispatcher) {
        val uploadList = List(10) { mockk<Uri>() }
        val uploadTask = mockk<UploadTask>()
        val exception = mockk<Exception>()
        every { ref.child(any<String>()) } returns ref
        every { ref.putFile(any<Uri>()) } returns uploadTask
        every { uploadTask.isComplete } returns false
        every { uploadTask.exception } returns exception
        every { uploadTask.isCanceled } returns true
        val result = repo.uploadListImage(uploadList).toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Failure)
    }

    @Test
    fun `uploadListImage with invalid listImages emits error 3`() = runTest(testDispatcher) {
        val uploadList = List(10) { mockk<Uri>() }
        val uploadTask = mockk<UploadTask>()
        val mockTaskSnapShot = mockk<TaskSnapshot>()
        val downloadUri = mockTask<Uri>()
        val resultTask = mockk<Uri>()
        val exception = mockk<Exception>()
        val url = ""
        every { ref.child(any<String>()) } returns ref
        every { ref.putFile(any<Uri>()) } returns uploadTask
        every { uploadTask.isComplete } returns true
        every { uploadTask.exception } returns null
        every { uploadTask.isCanceled } returns false
        every { uploadTask.result } returns mockTaskSnapShot
        every { mockTaskSnapShot.storage.downloadUrl } returns downloadUri
        every { downloadUri.result } throws exception
        every { resultTask.toString() } returns url
        val result = repo.uploadListImage(uploadList).toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Failure && (result.last() as Resource.Failure).error == exception)
    }

    @Test
    fun `downloadListImage with valid listFileRef emits success`() = runTest(testDispatcher) {
        val listFileRef = List(10) { "fileRef" }
        val downloadUri = mockTask<Uri>()
        val resultTask = mockk<Uri>()
        every { storage.getReferenceFromUrl(any<String>()) } returns ref
        every { ref.downloadUrl } returns downloadUri
        every { downloadUri.result } returns resultTask
        val result = repo.downloadListImage(listFileRef).toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Success && (result.last() as Resource.Success<List<Uri>>).value.size == listFileRef.size)
    }

    @Test
    fun `downloadListImage with invalid listFileRef emits error`() = runTest(testDispatcher) {
        val listFileRef = List(10) { "fileRef" }
        val downloadUri = mockTask<Uri>()
        val exception = mockk<Exception>()
        every { storage.getReferenceFromUrl(any<String>()) } returns ref
        every { ref.downloadUrl } returns downloadUri
        every { downloadUri.result } throws exception
        val result = repo.downloadListImage(listFileRef).toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Failure && (result.last() as Resource.Failure).error == exception)
    }

    @Test
    fun `downloadListImage with invalid listFileRef emits error 2`() = runTest(testDispatcher) {
        val listFileRef = List(10) { "fileRef" }
        val exception = mockk<Exception>()
        every { storage.getReferenceFromUrl(any<String>()) } returns ref
        every { ref.downloadUrl } throws exception
        val result = repo.downloadListImage(listFileRef).toList()
        assert(result.first() is Resource.Loading)
        assert(result.last() is Resource.Failure && (result.last() as Resource.Failure).error == exception)
    }
}