package com.kien.petclub.domain.usecase.firebase_db.product

class ProductUseCase(
    val addProductUseCase: AddProductUseCase,
    val deleteProductUseCase: DeleteProductUseCase,
    val getProductUseCase: GetProductUseCase,
    val updateProductUseCase: UpdateProductUseCase,
    val checkExistenceProductUseCase: CheckExistenceProductUseCase,
)