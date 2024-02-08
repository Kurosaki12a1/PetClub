package com.kien.petclub.domain.usecase.firebase_db.product

import com.kien.petclub.constants.Constants.VALUE_BRAND
import com.kien.petclub.constants.Constants.VALUE_TYPE
import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInfoProductUseCase @Inject constructor(private val repo: FirebaseDBRepository) {

    operator fun invoke(type: String): Flow<Resource<ArrayList<InfoProduct>>> {
        return when (type) {
            VALUE_BRAND -> repo.getBrandProducts()
            VALUE_TYPE -> repo.getTypeProducts()
            else -> repo.getLocationProducts()
        }
    }
}