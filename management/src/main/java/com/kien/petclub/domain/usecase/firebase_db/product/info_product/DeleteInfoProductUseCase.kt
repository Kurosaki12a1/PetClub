package com.kien.petclub.domain.usecase.firebase_db.product.info_product

import com.kien.petclub.constants.Constants.VALUE_BRAND
import com.kien.petclub.constants.Constants.VALUE_TYPE
import com.kien.petclub.domain.repository.FirebaseDBRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteInfoProductUseCase @Inject constructor(private val repo: FirebaseDBRepository) {
    operator fun invoke(type: String, id: String, parentId: String? = null): Flow<Resource<Unit>> {
        return when (type) {
            VALUE_BRAND -> repo.deleteBrandProduct(id)
            VALUE_TYPE -> repo.deleteTypeProduct(id, parentId)
            else -> repo.deleteLocationProduct(id)
        }
    }
}