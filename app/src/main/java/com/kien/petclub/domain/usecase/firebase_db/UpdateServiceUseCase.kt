package com.kien.petclub.domain.usecase.firebase_db

import com.kien.petclub.domain.model.entity.Service
import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class UpdateServiceUseCase @Inject constructor(
    private val firebaseDbRepository: FirebaseDBRepository
) {
    operator fun invoke(serviceId: String, data: Service) =
        firebaseDbRepository.updateServiceDatabase(serviceId, data)
}