package com.kien.petclub.presentation.home

import android.util.Log
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    private val _fabState = MutableStateFlow<Boolean>(true)

    val fabState: StateFlow<Boolean>
        get() = _fabState

    fun updateFabState() {
        _fabState.value = !_fabState.value
        Log.d("PetClub", "updateFabState: ${_fabState.value}")
    }

    fun onShrinkFab() {

    }
}