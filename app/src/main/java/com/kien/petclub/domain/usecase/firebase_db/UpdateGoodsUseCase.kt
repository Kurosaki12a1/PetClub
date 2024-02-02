package com.kien.petclub.domain.usecase.firebase_db

import com.kien.petclub.domain.model.entity.Goods
import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class UpdateGoodsUseCase @Inject constructor(
    private val firebaseDBRepository: FirebaseDBRepository
) {
    operator fun invoke(goodsId: String, data: Goods) =
        firebaseDBRepository.updateGoodsDatabase(goodsId, data)
}