package com.kien.petclub.presentation.product.common

import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ShareMultiDataViewModel : BaseViewModel() {
    private val _productResponse = MutableStateFlow<Product?>(null)
    val productResponse = _productResponse.asStateFlow()

    private val _infoProductResponse = MutableStateFlow<String?>(null)
    val infoProductResponse = _infoProductResponse.asStateFlow()

    fun setProduct(product: Product) {
        _productResponse.value = product
    }

    fun setInfoProduct(info: String?) {
        _infoProductResponse.value = info
    }
}