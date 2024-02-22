package com.kien.imagepicker.entity

import android.net.Uri

data class Album(val id: Long, val name: String, val images: ArrayList<Photo>, var isSelected: Boolean = false)

data class Photo(val id: Long, val uri: Uri, var isSelected: Boolean = false)
