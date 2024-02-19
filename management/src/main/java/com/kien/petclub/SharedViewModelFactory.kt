package com.kien.petclub

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/*
class SharedViewModelFactory<T>(private val application: Application) :
    ViewModelProvider.AndroidViewModelFactory(application) {

    override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel<T>(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")    }

}*/
