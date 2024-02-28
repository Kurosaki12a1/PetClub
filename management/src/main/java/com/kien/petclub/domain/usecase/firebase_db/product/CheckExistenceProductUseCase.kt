package com.kien.petclub.domain.usecase.firebase_db.product

import com.kien.petclub.domain.repository.GoodsRepository
import com.kien.petclub.domain.repository.ServiceRepository
import javax.inject.Inject

class CheckExistenceProductUseCase @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val goodsRepository: GoodsRepository
) {
    fun checkIdServiceExist(id: String) = serviceRepository.checkServiceExist(id)

    fun checkIdGoodsExist(id: String) = goodsRepository.checkGoodsExist(id)
}