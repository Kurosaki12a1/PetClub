package com.kien.petclub.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kien.petclub.data.data_source.local.dao.FilterDao
import com.kien.petclub.data.data_source.local.entity.FilterProductEntity

@Database(entities = [FilterProductEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val filterDao: FilterDao

    companion object {
        const val DB_NAME = "FilterProductDatabase.db"
    }
}