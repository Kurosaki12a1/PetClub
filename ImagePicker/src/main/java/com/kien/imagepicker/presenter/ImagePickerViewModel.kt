package com.kien.imagepicker.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kien.imagepicker.data.entity.Album
import com.kien.imagepicker.data.entity.Photo
import com.kien.imagepicker.domain.usecase.GetAlbumsUseCase
import com.kien.imagepicker.domain.usecase.GetPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagePickerViewModel @Inject constructor(
    private val getAlbumsUseCase: GetAlbumsUseCase,
    private val getPhotosUseCase: GetPhotosUseCase
) :
    ViewModel() {
    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums = _albums.asStateFlow()

    private val _photos = MutableStateFlow<PagingData<Photo>>(PagingData.empty())
    val photos = _photos.asStateFlow()

    fun getAlbums() {
        viewModelScope.launch {
            getAlbumsUseCase().collect {
                _albums.value = it
            }
        }
    }

    fun getPhotos(album: Album) {
        viewModelScope.launch {
            getPhotosUseCase(album).cachedIn(this).collect {
                _photos.value = it
            }
        }
    }
}