package com.kien.petclub.domain.repository

import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.model.entity.User
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseDBRepository {

    fun addUserDatabase(
        userId: String,
        email: String,
        name: String,
        phoneNumber: String
    ): Flow<Resource<Unit>>

    fun updateUserDatabase(userId: String, user: User): Flow<Resource<Unit>>

    fun getUserDatabase(userId: String): Flow<Resource<User?>>

    fun deleteUserDatabase(userId: String): Flow<Resource<Unit>>

    fun addGoodsDatabase(goods: Product.Goods): Flow<Resource<Unit>>

    fun addServiceDatabase(service: Product.Service): Flow<Resource<Unit>>

    fun updateGoodsDatabase(goodsId: String, data: Product.Goods): Flow<Resource<Product>>

    fun updateServiceDatabase(serviceId: String, data: Product.Service): Flow<Resource<Product>>

    fun getGoodsDatabase(): Flow<Resource<ArrayList<Product.Goods>>?>

    fun getServiceDatabase(): Flow<Resource<ArrayList<Product.Service>>?>

    fun getGoodsById(id: String): Flow<Resource<Product.Goods?>>

    fun getServiceById(id: String): Flow<Resource<Product.Service?>>

    fun deleteGoodsDatabase(goodsId: String): Flow<Resource<Unit>>

    fun deleteServiceDatabase(serviceId: String): Flow<Resource<Unit>>

    fun checkGoodsExist(id: String): Flow<Resource<Boolean>>

    fun checkServiceExist(id: String): Flow<Resource<Boolean>>

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