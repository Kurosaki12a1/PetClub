package com.kien.petclub

import android.app.Application
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.presentation.base.SharedViewModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class PetClubApplication : Application() {
    val sharedProductVM: SharedViewModel<Product> by lazy { SharedViewModel() }
}