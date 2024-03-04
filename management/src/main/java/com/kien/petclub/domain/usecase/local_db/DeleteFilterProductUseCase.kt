package com.kien.petclub.domain.usecase.local_db

import com.kien.petclub.data.data_source.local.entity.FilterProductEntity
import com.kien.petclub.domain.repository.FilterProductRepository
import javax.inject.Inject

class DeleteFilterProductUseCase @Inject constructor(private val repo: FilterProductRepository) {
    operator fun invoke(filterProductEntity: FilterProductEntity) = repo.delete(filterProductEntity)

    operator fun invoke(id: Long) = repo.delete(id)
}