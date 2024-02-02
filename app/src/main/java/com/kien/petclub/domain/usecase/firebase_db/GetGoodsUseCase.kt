package com.kien.petclub.domain.usecase.firebase_db

import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class GetGoodsUseCase @Inject constructor(
    private val firebaseDBRepository: FirebaseDBRepository
) {
    operator fun invoke() = firebaseDBRepository.getGoodsDatabase()

    operator fun invoke(goodsId: String) = firebaseDBRepository.getGoodsById(goodsId)
}