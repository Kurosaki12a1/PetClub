package com.kien.imagepicker.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import com.kien.imagepicker.entity.Album
import com.kien.imagepicker.entity.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ImagePickerRepository @Inject constructor(private val contentResolver: ContentResolver) {
    companion object {
        private const val ID_TOTAL_PHOTO_ALBUM = 230797250203L
        private const val NAME_TOTAL_PHOTO_ALBUM = "All Photos"
    }

    fun getAlbums(): Flow<List<Album>> = flow {
        val albumMap = mutableMapOf<Long, Album>()

        // Query albums from media store
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media._ID
        )

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Images.Media.DATE_ADDED + " ASC"
        )?.use { cursor ->
            val bucketNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val bucketName = cursor.getString(bucketNameColumn)
                val bucketId = cursor.getLong(bucketIdColumn)
                val contentUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                val photo = Photo(id, contentUri)
                val album = albumMap.getOrPut(bucketId) {
                    Album(bucketId, bucketName, arrayListOf())
                }
                album.images.add(photo)
            }
        }
        val albums = ArrayList<Album>()
        val totalPhotoAlbum = Album(
            ID_TOTAL_PHOTO_ALBUM,
            NAME_TOTAL_PHOTO_ALBUM,
            albumMap.values.flatMap { it.images }.toCollection(ArrayList()),
            true
        )
        albums.add(totalPhotoAlbum)
        albums.addAll(albumMap.values)
        emit(albums)
    }.flowOn(Dispatchers.IO)

}