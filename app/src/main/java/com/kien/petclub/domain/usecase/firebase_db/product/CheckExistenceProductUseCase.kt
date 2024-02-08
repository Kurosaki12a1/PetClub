package com.kien.petclub.domain.usecase.firebase_db.product

import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class CheckExistenceProductUseCase @Inject constructor(
    private val firebaseDBRepository: FirebaseDBRepository
) {
    fun checkIdServiceExist(id: String) = firebaseDBRepository.checkServiceExist(id)

    fun checkIdGoodsExist(id: String) = firebaseDBRepository.checkGoodsExist(id)
}