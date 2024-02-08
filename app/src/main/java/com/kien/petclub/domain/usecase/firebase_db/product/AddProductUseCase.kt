package com.kien.petclub.domain.usecase.firebase_db.product

import com.kien.petclub.domain.model.entity.Goods
import com.kien.petclub.domain.model.entity.Service
import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddProductUseCase @Inject constructor(
    private val firebaseDBRepository: FirebaseDBRepository
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
        val service = Service(
            id = id,
            code = code,
            name = name,
            type = type,
            brands = brands,
            sellingPrice = sellingPrice,
            buyingPrice = buyingPrice,
            description = description,
            note = note,
            photo = photo
        )
        return firebaseDBRepository.addServiceDatabase(service)
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
        photo: List<String>? = null
    ): Flow<Resource<Unit>> {
        val goods = Goods(
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
            photo = photo
        )
        return firebaseDBRepository.addGoodsDatabase(goods)
    }
}