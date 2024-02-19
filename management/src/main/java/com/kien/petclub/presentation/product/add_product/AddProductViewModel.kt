package com.kien.petclub.presentation.product.add_product

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.kien.petclub.domain.usecase.firebase_db.product.AddProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.CheckExistenceProductUseCase
import com.kien.petclub.domain.usecase.storage.UploadImageUseCase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val addProductUseCase: AddProductUseCase,
    private val checkExistenceProductUseCase: CheckExistenceProductUseCase,
    private val uploadImageUseCase: UploadImageUseCase
) : BaseViewModel() {
    private val _response = MutableStateFlow<Resource<Unit>>(Resource.Default)
    val response = _response.asStateFlow()

    fun addService(
        id: String,
        code: String?,
        name: String,
        sellingPrice: String,
        buyingPrice: String,
        description: String?,
        note: String?,
        type: String,
        brand: String,
        photo: List<Uri>? = null
    ) {
        val isListPhotoEmpty = photo.isNullOrEmpty()
        viewModelScope.launch {
            checkExistenceProductUseCase.checkIdServiceExist(id).flatMapConcat {
                when (it) {
                    is Resource.Success -> {
                        if (it.value) {
                            flow { emit(Resource.Failure(Exception("Product id already exists"))) }
                        } else {
                            if (!isListPhotoEmpty) {
                                uploadImageUseCase(photo!!)
                            } else {
                                flow { emit(Resource.success(listOf())) }
                            }
                        }
                    }

                    is Resource.Failure -> flow { emit(it) }
                    is Resource.Loading -> flow { emit(Resource.Loading) }
                    else -> flow { emit(Resource.Default) }
                }
            }.flatMapConcat {
                when (it) {
                    is Resource.Success -> {
                        addProductUseCase(
                            id = id,
                            code = code,
                            name = name,
                            type = type,
                            brands = brand,
                            sellingPrice = sellingPrice,
                            buyingPrice = buyingPrice,
                            description = description,
                            note = note,
                            photo = it.value.ifEmpty { null }
                        )
                    }

                    is Resource.Failure -> flow { emit(it) }
                    is Resource.Loading -> flow { emit(Resource.Loading) }
                    else -> flow { emit(Resource.Default) }
                }
            }.collect {
                _response.value = it
            }
        }
    }

    fun addGoods(
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
        photo: List<Uri>? = null,
        minimumStock: String? = "0",
        maximumStock: String? = "999999"
    ) {
        val isListPhotoEmpty = photo.isNullOrEmpty()
        viewModelScope.launch {
            checkExistenceProductUseCase.checkIdGoodsExist(id).flatMapConcat {
                when (it) {
                    is Resource.Success -> {
                        if (it.value) {
                            flow { emit(Resource.Failure(Exception("Product id already exists"))) }
                        } else {
                            if (!isListPhotoEmpty) {
                                uploadImageUseCase(photo!!)
                            } else {
                                flow { emit(Resource.success(listOf())) }
                            }
                        }
                    }

                    is Resource.Failure -> flow { emit(it) }
                    is Resource.Loading -> flow { emit(Resource.Loading) }
                    else -> flow { emit(Resource.Default) }
                }
            }.flatMapConcat {
                when (it) {
                    is Resource.Success -> {
                        addProductUseCase(
                            id = id,
                            code = code,
                            name = name,
                            type = type,
                            brands = brand,
                            sellingPrice = sellingPrice,
                            buyingPrice = buyingPrice,
                            stock = stock,
                            weight = weight,
                            location = location,
                            description = description,
                            note = note,
                            photo = it.value.ifEmpty { null },
                            minimumStock = minimumStock,
                            maximumStock = maximumStock
                        )
                    }

                    is Resource.Failure -> flow { emit(it) }
                    is Resource.Loading -> flow { emit(Resource.Loading) }
                    else -> flow { emit(Resource.Default) }
                }
            }.collect {
                _response.value = it
            }
        }
    }
}