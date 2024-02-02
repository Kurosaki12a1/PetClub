package com.kien.petclub.domain.repository

import com.kien.petclub.domain.model.entity.Goods
import com.kien.petclub.domain.model.entity.Service
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseDBRepository {
    fun addGoodsDatabase(goods: Goods): Flow<Resource<Unit>>

    fun addServiceDatabase(service: Service): Flow<Resource<Unit>>

    fun updateGoodsDatabase(goodsId: String, data: Goods): Flow<Resource<Unit>>

    fun updateServiceDatabase(serviceId: String, data: Service): Flow<Resource<Unit>>

    fun getGoodsDatabase(): Flow<ArrayList<Goods>?>

    fun getServiceDatabase(): Flow<ArrayList<Service>?>

    fun getGoodsById(id: String): Flow<Goods?>

    fun getServiceById(id : String): Flow<Service?>

    fun deleteGoodsDatabase(goodsId: String): Flow<Resource<Unit>>

    fun deleteServiceDatabase(serviceId: String): Flow<Resource<Unit>>
}