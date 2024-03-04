package com.kien.petclub.domain.usecase.firebase_db.product.info_product

import com.kien.petclub.constants.Constants.VALUE_BRAND
import com.kien.petclub.constants.Constants.VALUE_TYPE
import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.repository.InfoProductRepository
import com.kien.petclub.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddInfoProductUseCase @Inject constructor(val repo: InfoProductRepository) {
    operator fun invoke(type: String, name: String): Flow<Resource<Unit>> {
        return when (type) {
            VALUE_BRAND -> repo.addBrandProduct(name)
            VALUE_TYPE -> repo.addTypeProduct(InfoProduct(id = "", name = name, fullName = name))
            else -> repo.addLocationProduct(name)
        }
    }

    // This for add child of type product
    operator fun invoke(
        parentId: String,
        parentName: String,
        name: String
    ): Flow<Resource<Unit>> {
        return repo.addTypeProduct(
            InfoProduct(
                id = "",
                name = name,
                fullName = "${parentName}: $name",
                parentId = parentId
            )
        )
    }
}