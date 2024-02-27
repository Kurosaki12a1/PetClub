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

/**
 * EditProductViewModel: ViewModel responsible for handling product edit operations.
 *
 * This ViewModel is involved in the business logic for downloading images, updating product details, and uploading
 * images related to a product. It utilizes UseCases for each specific operation, encapsulating the logic in a clean
 * architecture approach.
 *
 * @author Thinh Huynh
 * @date 27/02/2024
 *
 * @param downloadImageUseCase UseCase for downloading images.
 * @param updateProductUseCase UseCase for updating product details.
 * @param uploadImageUseCase UseCase for uploading images.
 */
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

    /**
     * Initiates the process of downloading images for a product.
     *
     * @param listFileRef List of file references for the images to be downloaded.
     */
    fun downloadImage(listFileRef: List<String>) {
        viewModelScope.launch {
            downloadImageUseCase(listFileRef).collect {
                _downloadResponse.value = it
            }
        }
    }

    /**
     * Updates the product details, including uploading new images if available.
     *
     * This function differentiates between goods and services to apply the correct update logic and
     * data structure.
     *
     * @param typeProduct The type of product (goods/service) to determine the update logic.
     * @param id Unique identifier of the product.
     * @param code Product code.
     * @param name Product name.
     * @param type Product type/category.
     * @param brand Product brand.
     * @param sellingPrice Selling price of the product.
     * @param buyingPrice Buying price of the product.
     * @param stock Current stock quantity.
     * @param weight Product weight (applicable for goods).
     * @param location Storage location (applicable for goods).
     * @param description Product description.
     * @param note Additional notes about the product.
     * @param newestPhoto List of URIs for new photos to be uploaded (optional).
     * @param photo Existing photo references before update (optional).
     * @param minimumStock Minimum stock level before restocking is required (optional, for goods).
     * @param maximumStock Maximum stock level to maintain (optional, for goods).
     */
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
        newestPhoto: List<Uri>? = null,
        photo: List<String>? = null,
        minimumStock: String?,
        maximumStock: String?
    ) {
        if (typeProduct == Constants.VALUE_GOODS) {
            viewModelScope.launch {
                // Attempt to upload new images
                uploadImageUseCase(newestPhoto).flatMapConcat {
                    when (it) {
                        // On successful image upload, prepare a list of all image references
                        is Resource.Success -> {
                            val listPhoto = ArrayList<String>()
                            // Add Old Photo
                            listPhoto.addAll(photo ?: emptyList())
                            // Add New photo
                            listPhoto.addAll(it.value)
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
                                photo = listPhoto,
                                minimumStock = minimumStock,
                                maximumStock = maximumStock,
                                updatedDate = System.currentTimeMillis()
                            )
                            // Call updateProductUseCase to update the product in the repository
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
                    // Collect the result of the update operation and update
                    _updateResponse.value = it
                }
            }
        } else {
            // For services, the process is similar but uses the Product.Service data class
            viewModelScope.launch {
                uploadImageUseCase(newestPhoto).flatMapConcat {
                    when (it) {
                        is Resource.Success -> {
                            val listPhoto = ArrayList<String>()
                            listPhoto.addAll(photo ?: emptyList())
                            listPhoto.addAll(it.value)
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
                                photo = listPhoto,
                                updatedDate = System.currentTimeMillis()
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
