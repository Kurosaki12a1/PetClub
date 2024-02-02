package com.kien.petclub.domain.usecase.firebase_db

import com.kien.petclub.domain.model.entity.Goods
import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class AddGoodsUseCase @Inject constructor(
    private val firebaseDBRepository: FirebaseDBRepository
) {
    operator fun invoke(goods: Goods) = firebaseDBRepository.addGoodsDatabase(goods)
}