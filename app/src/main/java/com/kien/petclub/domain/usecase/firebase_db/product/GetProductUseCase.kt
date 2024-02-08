package com.kien.petclub.domain.usecase.firebase_db.product

class GetProductUseCase(
    val getGoodsUseCase: GetGoodsUseCase,
    val getServicesUseCase: GetServiceUseCase
)