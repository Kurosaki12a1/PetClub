package com.kien.imagepicker.presenter

import com.kien.imagepicker.data.entity.Album

interface ImagePickerListener {

    fun onTakePhoto() {}

    fun onAlbumClick(album: Album, position: Int) {}
}