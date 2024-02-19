package com.kien.petclub.domain.usecase.firebase_db.product

import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val firebaseDBRepository: FirebaseDBRepository
) {
    operator fun invoke(goodsId: String, data: Product.Goods) =
        firebaseDBRepository.updateGoodsDatabase(goodsId, data)

    operator fun invoke(serviceId: String, data: Product.Service) =
        firebaseDBRepository.updateServiceDatabase(serviceId, data)
}