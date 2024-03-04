package com.kien.petclub.presentation.product.filter_product

import androidx.lifecycle.viewModelScope
import com.kien.petclub.data.data_source.local.entity.FilterProductEntity
import com.kien.petclub.domain.usecase.local_db.GetFilterProductUseCase
import com.kien.petclub.domain.usecase.local_db.InsertFilterProductUseCase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseViewModel
import com.kien.petclub.utils.readJsonFromAssets
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterProductViewModel @Inject constructor(
    private val getFilterProductUseCase: GetFilterProductUseCase,
    private val insertFilterProductUseCase: InsertFilterProductUseCase
): BaseViewModel(){
    private val _allOptionsFilter = MutableStateFlow<Resource<List<FilterProductEntity>?>>(Resource.Default)
    val allOptionsFilter = _allOptionsFilter.asStateFlow()

    private val _insertFilterResponse = MutableStateFlow<Resource<Unit>>(Resource.Default)
    val insertFilterResponse = _insertFilterResponse.asStateFlow()

    init {
        getAllOptionsFilterProduct()
    }

    private fun getAllOptionsFilterProduct() {
        viewModelScope.launch {
            getFilterProductUseCase().flatMapLatest { data ->
                when (data) {
                    is Resource.Success -> {
                        if (data.value.isEmpty()) {
                            getFilterProductUseCase.getFromAssets()
                        } else {
                            flowOf(data)
                        }
                    }
                    else -> flowOf(data)
                }
            }.collect {
                _allOptionsFilter.value = it
            }
        }
    }

    fun insertListFilterProduct(list: List<FilterProductEntity>) {
        viewModelScope.launch {
            insertFilterProductUseCase(list).collect {
                _insertFilterResponse.value = it
            }
        }
    }
}