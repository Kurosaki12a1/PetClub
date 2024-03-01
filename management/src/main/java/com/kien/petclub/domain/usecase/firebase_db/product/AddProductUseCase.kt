package com.kien.petclub.domain.usecase.firebase_db.product

import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.repository.GoodsRepository
import com.kien.petclub.domain.repository.ServiceRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddProductUseCase @Inject constructor(
    private val goodsRepository: GoodsRepository,
    private val serviceRepository: ServiceRepository
) {

    operator fun invoke(
        id: String,
        code: String?,
        name: String,
        type: String,
        brands: String,
        sellingPrice: String,
        buyingPrice: String,
        description: String?,
        note: String?,
        photo: List<String>? = null
    ): Flow<Resource<Unit>> {
        val service = Product.Service(
            id = id,
            code = code,
            name = name,
            type = type,
            brands = brands,
            sellingPrice = sellingPrice,
            buyingPrice = buyingPrice,
            description = description,
            note = note,
            photo = photo,
            updatedDate = System.currentTimeMillis().toString()
        )
        return serviceRepository.addServiceDatabase(service)
    }

    operator fun invoke(
        id: String,
        code: String,
        name: String,
        type: String,
        brands: String,
        sellingPrice: String,
        buyingPrice: String,
        stock: String,
        weight: String,
        location: String,
        description: String,
        note: String,
        photo: List<String>? = null,
        minimumStock: String?,
        maximumStock: String?
    ): Flow<Resource<Unit>> {
        val goods = Product.Goods(
            id = id,
            code = code,
            name = name,
            type = type,
            brands = brands,
            sellingPrice = sellingPrice,
            buyingPrice = buyingPrice,
            stock = stock,
            weight = weight,
            location = location,
            description = description,
            note = note,
            photo = photo,
            minimumStock = minimumStock,
            maximumStock = maximumStock,
            updatedDate = System.currentTimeMillis().toString()
        )
        return goodsRepository.addGoodsDatabase(goods)
    }
}