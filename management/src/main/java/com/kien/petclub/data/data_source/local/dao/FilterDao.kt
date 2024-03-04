package com.kien.petclub.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kien.petclub.data.data_source.local.entity.FilterProductEntity
@Dao
interface FilterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(filterProduct: FilterProductEntity)

    @Query("SELECT * FROM FilterProduct")
    fun loadAll(): List<FilterProductEntity>

    @Delete
    fun delete(filterProduct: FilterProductEntity)

    @Query("DELETE FROM FilterProduct WHERE id = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM FilterProduct where id=:id")
    fun getFilterProductById(id: Long): FilterProductEntity

    @Query("SELECT * FROM FilterProduct where name=:name")
    fun getFilterProductByName(name: String): FilterProductEntity

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(filterProduct: FilterProductEntity)

}