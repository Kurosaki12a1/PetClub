package com.kien.petclub.domain.usecase.firebase_db.product

import com.kien.petclub.domain.repository.GoodsRepository
import javax.inject.Inject

class GetGoodsUseCase @Inject constructor(
    private val goodsRepository: GoodsRepository
) {
    operator fun invoke() = goodsRepository.getGoodsDatabase()

    operator fun invoke(goodsId: String) = goodsRepository.getGoodsById(goodsId)
}