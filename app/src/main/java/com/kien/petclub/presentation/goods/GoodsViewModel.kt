package com.kien.petclub.presentation.goods

import androidx.lifecycle.viewModelScope
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.usecase.firebase_db.product.GetProductUseCase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoodsViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase
) : BaseViewModel() {

    private val _productResponse =
        MutableStateFlow<Resource<ArrayList<Product>>>(Resource.Default)
    val productResponse = _productResponse.asStateFlow()

    fun getAllProduct() {
        viewModelScope.launch {
            getProductUseCase.getGoodsUseCase().flatMapMerge { goods ->
                getProductUseCase.getServicesUseCase().map { service ->
                    when {
                        goods is Resource.Success && service is Resource.Success -> {
                            val list = ArrayList<Product>()
                            list.addAll(goods.value)
                            list.addAll(service.value)
                            Resource.success(list)
                        }

                        goods is Resource.Failure -> {
                            Resource.failure(goods.error, goods.errorMessage)
                        }

                        service is Resource.Failure -> {
                            Resource.failure(service.error, service.errorMessage)
                        }

                        else -> Resource.Loading
                    }
                }
            }.collect {
                _productResponse.value = it
            }
        }
    }
}