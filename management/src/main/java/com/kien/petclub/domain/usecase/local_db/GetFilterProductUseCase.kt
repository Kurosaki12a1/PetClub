package com.kien.petclub.domain.usecase.local_db

import com.kien.petclub.constants.Constants
import com.kien.petclub.data.data_source.local.entity.FilterProductEntity
import com.kien.petclub.domain.repository.FilterProductRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilterProductUseCase @Inject constructor(private val repository: FilterProductRepository) {
    operator fun invoke() = repository.loadAll()

    operator fun invoke(id: Long) = repository.getFilterProductById(id)

    operator fun invoke(name: String): Flow<Resource<FilterProductEntity?>> {
        return when (name) {
            Constants.VALUE_TYPE -> repository.getFilterProductByName(Constants.NAME_TYPE_PRODUCT)
            Constants.VALUE_BRAND -> repository.getFilterProductByName(Constants.NAME_BRAND_PRODUCT)
            Constants.VALUE_LOCATION -> repository.getFilterProductByName(Constants.NAME_LOCATION_PRODUCT)
            else -> repository.getFilterProductByName(name)
        }
    }

    fun getFromAssets() = repository.getFromAssets()
}