package com.kien.petclub.presentation.product.add_product

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.kien.petclub.constants.Constants.EXISTED_PRODUCT
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

/**
 * ViewModel class responsible for managing the addition of services and goods within the application.
 * It provides functionality to add new service or goods entries, handling the existence check,
 * image upload (if applicable), and the final addition of the product to the database.
 * @author Thinh Huynh
 * @date 27/02/2024
 */
@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val addProductUseCase: AddProductUseCase,
    private val checkExistenceProductUseCase: CheckExistenceProductUseCase,
    private val uploadImageUseCase: UploadImageUseCase
) : BaseViewModel() {
    private val _response = MutableStateFlow<Resource<Unit>>(Resource.Default)
    val response = _response.asStateFlow()

    /**
     * Adds a new service to the system.
     *
     * This function first checks if the service ID already exists in the system. If it does, it emits a failure resource.
     * Otherwise, it proceeds to check if there are any photos to upload. If photos exist, it calls the uploadImageUseCase
     * to handle the upload process. After handling the photo upload or in cases where no photos are to be uploaded,
     * it then proceeds to add the service details into the system by calling addProductUseCase.
     *
     * @param id The unique identifier for the service.
     * @param code An optional code associated with the service.
     * @param name The name of the service.
     * @param sellingPrice The selling price of the service.
     * @param buyingPrice The buying price of the service.
     * @param description An optional description of the service.
     * @param note An optional note regarding the service.
     * @param type The type of the service.
     * @param brand The brand associated with the service.
     * @param photo An optional list of Uri objects representing photos of the service.
     */
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
        // Checks if the photo list is empty or null, indicating there are no photos to upload.
        val isListPhotoEmpty = photo.isNullOrEmpty()
        viewModelScope.launch {
            // Checks if the service ID already exists in the system.
            checkExistenceProductUseCase.checkIdServiceExist(id).flatMapConcat {
                when (it) {
                    // If the ID already exists, emits a failure resource.
                    is Resource.Success -> {
                        if (it.value) {
                            flow { emit(Resource.Failure(Exception(EXISTED_PRODUCT))) }
                        } else {
                            // If there are photos to upload and the ID does not exist, proceeds with the photo upload.
                            if (!isListPhotoEmpty) {
                                uploadImageUseCase(photo!!)
                            } else {
                                // If there are no photos to upload, emits a success resource with an empty list.
                                flow { emit(Resource.success(listOf())) }
                            }
                        }
                    }
                    // Emits the failure directly if checking the existence of the ID fails.
                    is Resource.Failure -> flow { emit(it) }
                    // Emits a loading resource during the existence check process.
                    is Resource.Loading -> flow { emit(Resource.Loading) }
                    else -> flow { emit(Resource.Default) }
                }
            }.flatMapConcat {
                when (it) {
                    // Upon successful photo upload or if no upload is needed, adds the product details to the system.
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
                // Collects the final outcome of the add service operation and updates the LiveData.
                _response.value = it
            }
        }
    }

    /**
     * Adds a new goods entry to the system.
     *
     * Similar to addService, this function checks for the existence of the goods ID, handles photo uploads if necessary,
     * and adds the goods details to the database. It additionally handles properties specific to goods such as stock,
     * weight, and location.
     *
     * @param id The unique identifier for the goods.
     * @param code A code associated with the goods.
     * @param name The name of the goods.
     * @param type The type of the goods.
     * @param brand The brand associated with the goods.
     * @param sellingPrice The selling price of the goods.
     * @param buyingPrice The buying price of the goods.
     * @param stock The stock level of the goods.
     * @param weight The weight of the goods.
     * @param location The storage location of the goods.
     * @param description A description of the goods.
     * @param note A note regarding the goods.
     * @param photo An optional list of Uri objects for the goods' photos.
     * @param minimumStock An optional minimum stock level for the goods.
     * @param maximumStock An optional maximum stock level for the goods.
     */
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
            // Checks if the photo list is empty or null, indicating there are no photos to upload.
            checkExistenceProductUseCase.checkIdGoodsExist(id).flatMapConcat {
                when (it) {
                    // If the ID already exists, emits a failure resource.
                    is Resource.Success -> {
                        if (it.value) {
                            flow { emit(Resource.Failure(Exception(EXISTED_PRODUCT))) }
                        } else {
                            // If there are photos to upload and the ID does not exist, proceeds with the photo upload.
                            if (!isListPhotoEmpty) {
                                uploadImageUseCase(photo!!)
                            } else {
                                // If there are no photos to upload, emits a success resource with an empty list.
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
                    // Upon successful photo upload or if no upload is needed, adds the product details to the system.
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