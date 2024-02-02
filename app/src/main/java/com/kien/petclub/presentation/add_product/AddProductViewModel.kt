package com.kien.petclub.presentation.add_product

import androidx.lifecycle.viewModelScope
import com.kien.petclub.domain.model.entity.Service
import com.kien.petclub.domain.usecase.firebase_db.AddServiceUseCase
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(private val useCase: AddServiceUseCase) :
    BaseViewModel() {
    fun submit(
        id: String,
        code: String?,
        name: String,
        sellingPrice: String,
        buyingPrice: String,
        description: String?,
        note: String?,
        type: String,
        brand: String,
        photo: List<String>?
    ) {
       useCase.invoke(Service(
              id = id,
              code = code,
              name = name,
              sellingPrice = sellingPrice,
              buyingPrice = buyingPrice,
              description = description,
              note = note,
              type = type,
              brands = brand,
              photo = photo
       )).launchIn(viewModelScope)
    }
}