package com.kien.petclub.domain.usecase.firebase_db.product.info_product

class InfoProductUseCase (
    val addUseCase: AddInfoProductUseCase,
    val deleteUseCase: DeleteInfoProductUseCase,
    val getUseCase: GetInfoProductUseCase,
    val searchUseCase: SearchInfoProductUseCase
)