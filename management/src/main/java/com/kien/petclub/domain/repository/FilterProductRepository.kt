package com.kien.petclub.domain.repository

import com.kien.petclub.data.data_source.local.entity.FilterProductEntity
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface FilterProductRepository {
    fun add(filterProduct: FilterProductEntity): Flow<Resource<Unit>>

    fun add(filterProducts: List<FilterProductEntity>): Flow<Resource<Unit>>

    fun update(filterProduct: FilterProductEntity): Flow<Resource<Unit>>

    fun delete(filterProduct: FilterProductEntity): Flow<Resource<Unit>>

    fun delete(id : Long) : Flow<Resource<Unit>>

    fun loadAll(): Flow<Resource<List<FilterProductEntity>>>

    fun getFilterProductById(id: Long): Flow<Resource<FilterProductEntity?>>

    fun getFilterProductByName(name: String): Flow<Resource<FilterProductEntity?>>

    fun getFromAssets(): Flow<Resource<List<FilterProductEntity>?>>
}