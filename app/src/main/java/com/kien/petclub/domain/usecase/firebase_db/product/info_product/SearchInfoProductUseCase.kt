package com.kien.petclub.domain.usecase.firebase_db.product.info_product

import com.kien.petclub.constants.Constants.VALUE_BRAND
import com.kien.petclub.constants.Constants.VALUE_TYPE
import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchInfoProductUseCase @Inject constructor(private val repo: FirebaseDBRepository) {

    operator fun invoke(type: String, query: String): Flow<Resource<ArrayList<InfoProduct>>> {
        return when (type) {
            VALUE_BRAND -> {
                repo.searchForBrands(query)
            }
            VALUE_TYPE -> {
                repo.searchForTypes(query)
            }
            else -> {
                repo.searchForLocations(query)
            }
        }
    }

}