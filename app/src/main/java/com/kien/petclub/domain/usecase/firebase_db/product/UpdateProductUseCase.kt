package com.kien.petclub.domain.usecase.firebase_db.product

import com.kien.petclub.domain.model.entity.Goods
import com.kien.petclub.domain.model.entity.Service
import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val firebaseDBRepository: FirebaseDBRepository
) {
    operator fun invoke(goodsId: String, data: Goods) =
        firebaseDBRepository.updateGoodsDatabase(goodsId, data)

    operator fun invoke(serviceId: String, data: Service) =
        firebaseDBRepository.updateServiceDatabase(serviceId, data)
}