package com.kien.petclub.domain.usecase.firebase_db

import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class DeleteServiceUseCase @Inject constructor(
    private val firebaseDBRepository: FirebaseDBRepository
) {
    operator fun invoke(serviceId: String) = firebaseDBRepository.deleteServiceDatabase(serviceId)
}