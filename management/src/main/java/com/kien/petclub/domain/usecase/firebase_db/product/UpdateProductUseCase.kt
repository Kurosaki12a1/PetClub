package com.kien.petclub.domain.usecase.firebase_db.product

import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.repository.GoodsRepository
import com.kien.petclub.domain.repository.ServiceRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val goodsRepository: GoodsRepository,
    private val serviceRepository: ServiceRepository
) {
    operator fun invoke(goodsId: String, data: Product.Goods) =
        goodsRepository.updateGoodsDatabase(goodsId, data)

    operator fun invoke(serviceId: String, data: Product.Service) =
        serviceRepository.updateServiceDatabase(serviceId, data)
}