package com.kien.petclub.presentation.product.add_info_product

import androidx.lifecycle.viewModelScope
import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.usecase.firebase_db.product.info_product.AddInfoProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.info_product.DeleteInfoProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.info_product.GetInfoProductUseCase
import com.kien.petclub.domain.usecase.firebase_db.product.info_product.SearchInfoProductUseCase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddInfoProductViewModel @Inject constructor(
    private val addInfoProductUseCase: AddInfoProductUseCase,
    private val searchInfoProductUseCase: SearchInfoProductUseCase,
    private val getInfoProductUseCase: GetInfoProductUseCase,
    private val deleteInfoProductUseCase: DeleteInfoProductUseCase
) : BaseViewModel() {

    private val _addResponse = MutableStateFlow<Resource<Unit>>(Resource.Default)
    val addResponse = _addResponse.asStateFlow()

    private val _searchResponse =
        MutableStateFlow<Resource<ArrayList<InfoProduct>>>(Resource.Default)
    val searchResponse = _searchResponse.asStateFlow()

    private val _getResponse = MutableStateFlow<Resource<ArrayList<InfoProduct>>>(Resource.Default)
    val getResponse = _getResponse.asStateFlow()

    private val _deleteResponse = MutableStateFlow<Resource<Unit>>(Resource.Default)
    val deleteResponse = _deleteResponse.asStateFlow()


    fun addInfo(type: String, name: String) {
        viewModelScope.launch {
            addInfoProductUseCase(type, name).collect {
                _addResponse.value = it
            }
        }
    }

    fun updateTypeProduct(parentId: String, name: String) {
        viewModelScope.launch {
            addInfoProductUseCase.addTypeChildProduct(parentId, name).collect {
                _addResponse.value = it
            }
        }
    }

    fun searchInfo(type: String, name: String) {
        viewModelScope.launch {
            searchInfoProductUseCase(type, name).collect {
                _searchResponse.value = it
            }
        }
    }

    fun getInfo(type: String) {
        viewModelScope.launch {
            getInfoProductUseCase(type).collect {
                _getResponse.value = it
            }
        }
    }

    fun deleteInfo(type: String, id: String, parentId: String? = null) {
        viewModelScope.launch {
            deleteInfoProductUseCase(type, id, parentId).collect {
                _deleteResponse.value = it
            }
        }
    }
}

