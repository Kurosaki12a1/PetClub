package com.kien.imagepicker.domain.repository

import androidx.paging.PagingData
import com.kien.imagepicker.data.entity.Album
import com.kien.imagepicker.data.entity.Photo
import kotlinx.coroutines.flow.Flow

interface ImagePickerRepository {

    fun getAlbums(): Flow<List<Album>>

    fun getPhotos(album: Album): Flow<PagingData<Photo>>
}