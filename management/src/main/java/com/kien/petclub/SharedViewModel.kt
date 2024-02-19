package com.kien.petclub

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class SharedViewModel<T>(application: Application) : AndroidViewModel(application) {
    private val _data = MutableStateFlow<T?>(null)
    val data = _data.asStateFlow()

    fun setData(data: T) {
        _data.value = data
    }
}