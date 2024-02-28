package com.kien.petclub.domain.repository

import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface GoodsRepository {

    fun addGoodsDatabase(goods: Product.Goods): Flow<Resource<Unit>>

    fun updateGoodsDatabase(goodsId: String, data: Product.Goods): Flow<Resource<Product>>

    fun getGoodsDatabase(): Flow<Resource<ArrayList<Product.Goods>>?>

    fun getGoodsById(id: String): Flow<Resource<Product.Goods?>>

    fun deleteGoodsDatabase(goodsId: String): Flow<Resource<Unit>>

    fun checkGoodsExist(id: String): Flow<Resource<Boolean>>
}