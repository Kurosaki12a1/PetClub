package com.kien.petclub.domain.usecase.firebase_db

import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class DeleteGoodsUseCase @Inject constructor(
    private val firebaseDBRepository: FirebaseDBRepository
) {
    operator fun invoke(goodsId: String) = firebaseDBRepository.deleteGoodsDatabase(goodsId)
}