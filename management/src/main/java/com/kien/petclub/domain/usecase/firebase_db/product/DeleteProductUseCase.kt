package com.kien.petclub.domain.usecase.firebase_db.product

import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val firebaseDBRepository: FirebaseDBRepository
) {
    fun deleteGoodsWithId(goodsId: String) = firebaseDBRepository.deleteGoodsDatabase(goodsId)

    fun deleteServiceWithId(serviceId: String) = firebaseDBRepository.deleteServiceDatabase(serviceId)
}