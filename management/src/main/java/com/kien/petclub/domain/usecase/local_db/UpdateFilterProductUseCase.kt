package com.kien.petclub.domain.usecase.local_db

import com.kien.petclub.data.data_source.local.entity.FilterProductEntity
import com.kien.petclub.domain.repository.FilterProductRepository
import javax.inject.Inject

class UpdateFilterProductUseCase @Inject constructor(private val repo : FilterProductRepository) {
    operator fun invoke(filterProduct: FilterProductEntity) = repo.update(filterProduct)
}