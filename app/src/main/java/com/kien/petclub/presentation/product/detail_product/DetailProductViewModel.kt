package com.kien.petclub.presentation.product.detail_product

import androidx.lifecycle.viewModelScope
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.usecase.firebase_db.product.DeleteProductUseCase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val deleteProductUseCase: DeleteProductUseCase
) : BaseViewModel() {
    private val _deleteResponse = MutableStateFlow<Resource<Unit>>(Resource.Default)
    val deleteResponse = _deleteResponse.asStateFlow()

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            when (product) {
                is Product.Goods -> {
                    deleteProductUseCase.deleteGoodsWithId(product.id).collect {
                        _deleteResponse.value = it
                    }
                }

                is Product.Service -> {
                    deleteProductUseCase.deleteServiceWithId(product.id).collect {
                        _deleteResponse.value = it
                    }
                }
            }

        }
    }
}