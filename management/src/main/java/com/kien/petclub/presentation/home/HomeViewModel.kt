package com.kien.petclub.presentation.home

import android.graphics.Rect
import android.view.MotionEvent
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    private val _fabState = MutableStateFlow(false)

    val fabState: StateFlow<Boolean>
        get() = _fabState

    fun updateFabState() {
        _fabState.value = !_fabState.value
    }

    fun shrinkFab(ev: MotionEvent, outRect: Rect) {
        // When the user touches the screen and the FAB is extended, we shrink it
        if (fabState.value && !outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
            updateFabState()
        }

    }
}