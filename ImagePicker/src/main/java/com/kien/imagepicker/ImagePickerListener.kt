package com.kien.imagepicker

import com.kien.imagepicker.entity.Album

interface ImagePickerListener {

    fun onTakePhoto() {}

    fun onAlbumClick(album: Album, position: Int) {}
}