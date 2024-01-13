package com.kien.petclub.domain.repository

import com.kien.petclub.domain.model.entity.Goods
import com.kien.petclub.domain.model.entity.Service
import kotlinx.coroutines.flow.Flow

interface FirebaseDBRepository {
    fun addGoodsDatabase(goods : Goods): Flow<Boolean>

    fun addServiceDatabase(service : Service): Flow<Boolean>

    fun updateGoodsDatabase(goodsId: String): Flow<Boolean>

    fun updateServiceDatabase(serviceId: String): Flow<Boolean>

    fun getGoodsDatabase(userId: String): Flow<ArrayList<Goods>>

    fun getServiceDatabase(userId: String): Flow<ArrayList<Service>>

    fun deleteGoodsDatabase(goodsId: String): Flow<Boolean>

    fun deleteServiceDatabase(serviceId: String): Flow<Boolean>
}