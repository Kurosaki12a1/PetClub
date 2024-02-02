package com.kien.petclub.domain.usecase.firebase_db

import com.kien.petclub.domain.model.entity.Service
import com.kien.petclub.domain.repository.FirebaseDBRepository
import javax.inject.Inject

class AddServiceUseCase @Inject constructor(
    private val firebaseDBRepository: FirebaseDBRepository
) {
    operator fun invoke(service: Service) = firebaseDBRepository.addServiceDatabase(service)
}