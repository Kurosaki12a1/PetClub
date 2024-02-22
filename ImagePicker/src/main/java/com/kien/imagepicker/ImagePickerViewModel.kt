package com.kien.imagepicker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kien.imagepicker.entity.Album
import com.kien.imagepicker.repository.ImagePickerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagePickerViewModel @Inject constructor(private val repository: ImagePickerRepository) : ViewModel() {
    private val _images = MutableStateFlow<List<Album>>(emptyList())
    val images = _images.asStateFlow()

    fun getAlbums() {
        viewModelScope.launch {
            repository.getAlbums().collect {
                _images.value = it
            }
        }
    }
}