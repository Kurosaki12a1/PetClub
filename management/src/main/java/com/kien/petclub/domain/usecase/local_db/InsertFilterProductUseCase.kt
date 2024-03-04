package com.kien.petclub.domain.usecase.local_db

import com.kien.petclub.data.data_source.local.entity.FilterProductEntity
import com.kien.petclub.domain.repository.FilterProductRepository
import javax.inject.Inject

class InsertFilterProductUseCase @Inject constructor(private val repo : FilterProductRepository) {
    operator fun invoke(filterProduct: FilterProductEntity) = repo.add(filterProduct)

    operator fun invoke(list : List<FilterProductEntity>) = repo.add(list)
}