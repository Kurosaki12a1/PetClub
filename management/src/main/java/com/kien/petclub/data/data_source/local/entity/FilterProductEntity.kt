package com.kien.petclub.data.data_source.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize

@Entity(tableName = "FilterProduct")
@TypeConverters(ArrayStringConverter::class)
@Parcelize
data class FilterProductEntity(
    @PrimaryKey val id: Long,
    var name: String,
    var options: ArrayList<String>,
    var selectedOptions : ArrayList<String>,
    var isMultiOptions: Boolean
) : Parcelable