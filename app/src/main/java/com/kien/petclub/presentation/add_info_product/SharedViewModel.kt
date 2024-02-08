package com.kien.petclub.presentation.add_info_product

import com.kien.petclub.constants.Constants
import com.kien.petclub.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel : BaseViewModel() {
    private val _dataPopup = MutableStateFlow(Constants.EMPTY_STRING)
    val dataPopup = _dataPopup.asStateFlow()

    fun setData(value : String) {
        _dataPopup.value = value
    }
}