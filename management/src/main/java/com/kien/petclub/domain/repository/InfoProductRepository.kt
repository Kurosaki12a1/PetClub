package com.kien.petclub.domain.repository

import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface InfoProductRepository {
    fun getTypeProducts(): Flow<Resource<ArrayList<InfoProduct>>>

    fun addTypeProduct(type: InfoProduct): Flow<Resource<Unit>>

    fun getBrandProducts(): Flow<Resource<ArrayList<InfoProduct>>>

    fun addBrandProduct(name: String): Flow<Resource<Unit>>

    fun getLocationProducts(): Flow<Resource<ArrayList<InfoProduct>>>

    fun addLocationProduct(name: String): Flow<Resource<Unit>>

    fun searchForBrands(name: String): Flow<Resource<ArrayList<InfoProduct>>>

    fun searchForTypes(name: String): Flow<Resource<ArrayList<InfoProduct>>>

    fun searchForLocations(name: String): Flow<Resource<ArrayList<InfoProduct>>>

    fun deleteTypeProduct(id: String, parentId: String?): Flow<Resource<Unit>>

    fun deleteBrandProduct(id: String): Flow<Resource<Unit>>

    fun deleteLocationProduct(id: String): Flow<Resource<Unit>>
}