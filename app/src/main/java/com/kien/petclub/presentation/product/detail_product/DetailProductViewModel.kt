package com.kien.petclub.presentation.product.detail_product

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.model.entity.getPhoto
import com.kien.petclub.domain.usecase.firebase_db.product.DeleteProductUseCase
import com.kien.petclub.domain.usecase.storage.DownloadImageUseCase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailProductViewModel @Inject constructor(
    private val deleteProductUseCase: DeleteProductUseCase,
    private val downloadImageUseCase: DownloadImageUseCase
) : BaseViewModel() {
    private val _deleteResponse = MutableStateFlow<Resource<Unit>>(Resource.Default)
    val deleteResponse = _deleteResponse.asStateFlow()

    private val _getPhotoResponse = MutableStateFlow<Resource<List<Uri>>>(Resource.Default)
    val getPhotoResponse = _getPhotoResponse.asStateFlow()

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

    fun getListPhoto(product: Product) {
        viewModelScope.launch {
            downloadImageUseCase(product.getPhoto()).collect {
                _getPhotoResponse.value = it
            }
        }
    }
}