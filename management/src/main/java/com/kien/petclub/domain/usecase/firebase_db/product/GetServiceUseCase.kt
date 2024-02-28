package com.kien.petclub.domain.usecase.firebase_db.product

import com.kien.petclub.domain.repository.ServiceRepository
import javax.inject.Inject

class GetServiceUseCase @Inject constructor(private val serviceRepository: ServiceRepository) {
    operator fun invoke() = serviceRepository.getServiceDatabase()

    operator fun invoke(serviceId: String) = serviceRepository.getServiceById(serviceId)
}