package com.kien.petclub.presentation.base

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SharedViewModel<T> : BaseViewModel() {
    private val _data = MutableStateFlow<T?>(null)
    val data = _data.asStateFlow()

    fun setData(data: T) {
        _data.value = data
    }
}