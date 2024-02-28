package com.kien.petclub.domain.repository

import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface ServiceRepository {

    fun addServiceDatabase(service: Product.Service): Flow<Resource<Unit>>

    fun updateServiceDatabase(serviceId: String, data: Product.Service): Flow<Resource<Product>>

    fun getServiceDatabase(): Flow<Resource<ArrayList<Product.Service>>?>

    fun getServiceById(id: String): Flow<Resource<Product.Service?>>

    fun deleteServiceDatabase(serviceId: String): Flow<Resource<Unit>>

    fun checkServiceExist(id: String): Flow<Resource<Boolean>>
}