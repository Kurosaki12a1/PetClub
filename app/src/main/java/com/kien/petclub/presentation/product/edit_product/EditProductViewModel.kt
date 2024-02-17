package com.kien.petclub.presentation.product.edit_product

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.kien.petclub.constants.Constants
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.usecase.firebase_db.product.UpdateProductUseCase
import com.kien.petclub.domain.usecase.storage.DownloadImageUseCase
import com.kien.petclub.domain.usecase.storage.UploadImageUseCase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProductViewModel @Inject constructor(
    private val downloadImageUseCase: DownloadImageUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val uploadImageUseCase: UploadImageUseCase
) : BaseViewModel() {
    private val _downloadResponse = MutableStateFlow<Resource<List<Uri>>>(Resource.Default)
    val downloadResponse = _downloadResponse.asStateFlow()

    private val _updateResponse = MutableStateFlow<Resource<Product>>(Resource.Default)
    val updateResponse = _updateResponse.asStateFlow()

    fun downloadImage(listFileRef: List<String>) {
        viewModelScope.launch {
            downloadImageUseCase(listFileRef).collect {
                _downloadResponse.value = it
            }
        }
    }

    fun updateProduct(
        typeProduct: String,
        id: String,
        code: String,
        name: String,
        type: String,
        brand: String,
        sellingPrice: String,
        buyingPrice: String,
        stock: String,
        weight: String,
        location: String,
        description: String,
        note: String,
        photo: List<Uri>? = null
    ) {
        if (typeProduct == Constants.VALUE_GOODS) {
            viewModelScope.launch {
                uploadImageUseCase(photo).flatMapConcat {
                    when (it) {
                        is Resource.Success -> {
                            val goods = Product.Goods(
                                id,
                                code,
                                name,
                                type,
                                brand,
                                sellingPrice,
                                buyingPrice,
                                stock,
                                weight,
                                location,
                                description,
                                note,
                                photo = it.value.ifEmpty { null }
                            )
                            updateProductUseCase(id, goods)
                        }

                        is Resource.Failure -> {
                            flowOf(Resource.Failure(it.error, it.errorMessage))
                        }

                        else -> {
                            flowOf(Resource.Loading)
                        }
                    }
                }.collect {
                    _updateResponse.value = it
                }
            }
        } else {
            viewModelScope.launch {
                uploadImageUseCase(photo).flatMapConcat {
                    when (it) {
                        is Resource.Success -> {
                            val service = Product.Service(
                                id,
                                code,
                                name,
                                type,
                                brand,
                                sellingPrice,
                                buyingPrice,
                                description,
                                note,
                                photo = it.value.ifEmpty { null }
                            )
                            updateProductUseCase(id, service)
                        }

                        is Resource.Failure -> {
                            flowOf(Resource.Failure(it.error, it.errorMessage))
                        }

                        else -> {
                            flowOf(Resource.Loading)
                        }
                    }
                }.collect {
                    _updateResponse.value = it
                }
            }
        }
    }
}
