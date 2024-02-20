package com.kien.petclub.presentation.product.utils

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.kien.petclub.presentation.home.HomeActivity

fun Fragment.showLoadingAnimation() {
    if (requireActivity() is HomeActivity){
        (requireActivity() as HomeActivity).showLoadingAnimation()
    }
}

fun Fragment.hideLoadingAnimation() {
    if (requireActivity() is HomeActivity){
        (requireActivity() as HomeActivity).stopLoadingAnimation()
    }
}

fun Fragment.showBottomNavigationAndFabButton() {
    if (requireActivity() is HomeActivity){
        (requireActivity() as HomeActivity).showBottomNavigationAndFabButton()
    }
}

fun Fragment.hideBottomNavigationAndFabButton() {
    if (requireActivity() is HomeActivity){
        (requireActivity() as HomeActivity).hideBottomNavigationAndFabButton()
    }
}

fun Fragment.showDialog(popUp: DialogFragment, tag: String) {
    if (!popUp.isVisible) {
        popUp.show(requireActivity().supportFragmentManager, tag)
    }
}