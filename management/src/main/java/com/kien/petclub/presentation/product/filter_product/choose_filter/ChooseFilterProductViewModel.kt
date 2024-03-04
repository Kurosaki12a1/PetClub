package com.kien.petclub.presentation.product.filter_product.choose_filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kien.petclub.constants.Constants
import com.kien.petclub.data.data_source.local.entity.FilterProductEntity
import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.model.entity.InfoProductItem
import com.kien.petclub.domain.usecase.firebase_db.product.info_product.GetInfoProductUseCase
import com.kien.petclub.domain.usecase.local_db.GetFilterProductUseCase
import com.kien.petclub.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChooseFilterProductViewModel @Inject constructor(
    private val getInfoProductUseCase: GetInfoProductUseCase,
    private val getFilterProductUseCase: GetFilterProductUseCase
) : ViewModel() {
    private val _getInfoProduct = MutableStateFlow<Resource<List<InfoProduct>>>(Resource.Default)
    val getInfoProduct = _getInfoProduct.asStateFlow()

    private val _getDataSelected =
        MutableStateFlow<Resource<FilterProductEntity?>>(Resource.Default)
    val getDataSelected = _getDataSelected.asStateFlow()

    fun getInfoProduct(type: String) {
        viewModelScope.launch {
            getInfoProductUseCase(type).collect {
                when (it) {
                    is Resource.Success -> {
                        val allOption = createOptionAll()
                        val result = listOf(allOption) + it.value
                        _getInfoProduct.value = Resource.Success(result)
                    }

                    else -> _getInfoProduct.value = it
                }
            }
        }
    }

    fun getFilterSelectedFromDB(type: String) {
        viewModelScope.launch {
            getFilterProductUseCase(type).collect {
                _getDataSelected.value = it
            }
        }
    }

    fun executeItemData(list: ArrayList<InfoProductItem>, item: InfoProductItem) {
        // This is parent item
        list.find { it == item || it.child?.contains(item) == true }?.let { data ->
            // Parent item
            if (data.id == item.id) {
                data.isSelected = !data.isSelected
            } else {
                // Child item
                data.child?.find { child -> child.id == item.id }?.let {
                    it.isSelected = !it.isSelected
                }
            }
        }
        // If data selected has many child data
        updateChildItems(list, item)
        // If data selected has parent data and all child data is selected
        updateParentItem(list, item.parentId)
    }

    fun executeFilterChosen(listChosen: ArrayList<String>, listData: ArrayList<InfoProductItem>) {
        listChosen.clear()
        val itemList = listData.filter { it.id != Constants.ID_OPTIONS_ALL }
        val realSize = itemList.size + itemList.sumOf { it.child?.size ?: 0 }
        itemList.forEach {
            if (it.isSelected) {
                listChosen.add(it.fullName)
            } else {
                listChosen.remove(it.fullName)
            }
            it.child?.forEach { child ->
                if (child.isSelected) {
                    listChosen.add(child.fullName)
                } else {
                    listChosen.remove(child.fullName)
                }
            }
        }.let {
            listData[ChooseFilterProductFragment.INDEX_OPTION_ALL].isSelected =
                listChosen.size == realSize
            if (listData[ChooseFilterProductFragment.INDEX_OPTION_ALL].isSelected) {
                // Add option "tất cả when all option is selected
                listChosen.clear()
                listChosen.add(Constants.NAME_OPTIONS_ALL)
            } else {
                listChosen.remove(Constants.NAME_OPTIONS_ALL)
            }
        }
    }

    private fun updateParentItem(list: ArrayList<InfoProductItem>, parentId: String?) {
        if (parentId == null) return
        list
            .find { it.id == parentId }
            .let {
                it?.isSelected = it?.child?.all { child -> child.isSelected } ?: false
            }
    }

    private fun updateChildItems(list: ArrayList<InfoProductItem>, parentItem: InfoProductItem) {
        if (parentItem.child == null) return
        list
            .find { it.id == parentItem.id }
            .let {
                it?.child?.forEach { child ->
                    child.isSelected = parentItem.isSelected
                }
            }
    }

    private fun createOptionAll(): InfoProduct {
        return InfoProduct(
            id = Constants.ID_OPTIONS_ALL,
            name = Constants.NAME_OPTIONS_ALL,
            fullName = Constants.NAME_OPTIONS_ALL,
            parentId = null,
            child = null
        )
    }


}