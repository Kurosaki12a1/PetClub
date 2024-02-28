package com.kien.petclub.domain.usecase.firebase_db.product

import com.kien.petclub.domain.repository.GoodsRepository
import com.kien.petclub.domain.repository.ServiceRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val goodsRepository: GoodsRepository,
    private val serviceRepository: ServiceRepository
) {
    fun deleteGoodsWithId(goodsId: String) = goodsRepository.deleteGoodsDatabase(goodsId)

    fun deleteServiceWithId(serviceId: String) = serviceRepository.deleteServiceDatabase(serviceId)
}