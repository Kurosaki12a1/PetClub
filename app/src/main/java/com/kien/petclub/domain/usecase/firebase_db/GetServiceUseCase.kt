package com.kien.petclub.domain.usecase.firebase_db

import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class GetServiceUseCase @Inject constructor(private val firebaseDBRepository: FirebaseDBRepository) {
    operator fun invoke() = firebaseDBRepository.getServiceDatabase()

    operator fun invoke(serviceId: String) = firebaseDBRepository.getServiceById(serviceId)
}