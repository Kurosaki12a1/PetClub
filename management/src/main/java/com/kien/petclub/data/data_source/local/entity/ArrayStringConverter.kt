package com.kien.petclub.data.data_source.local.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

class ArrayStringConverter {
    private val gson = Gson()
    @TypeConverter
    fun fromArrayString(options: ArrayList<String>?): String {
        if (options == null) {
            return ""
        }
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return gson.toJson(options, type)
    }

    @TypeConverter
    fun toArrayString(optionsString: String?): ArrayList<String> {
        if (optionsString.isNullOrEmpty()) {
            return arrayListOf()
        }
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return gson.fromJson(optionsString, type)
    }
}