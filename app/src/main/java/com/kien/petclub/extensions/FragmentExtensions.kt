package com.kien.petclub.extensions

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.kien.petclub.utils.hideSoftInput

fun Fragment.navigateSafe(directions: NavDirections, navOptions: NavOptions? = null) {
    findNavController().navigate(directions, navOptions)
}

fun Fragment.backToPreviousScreen() {
    findNavController().navigateUp()
}

fun <A : Activity> Fragment.openActivityAndClearStack(activity: Class<A>) {
    requireActivity().openActivityAndClearStack(activity)
}

fun <A : Activity> Fragment.openActivity(
    activity: Class<A>,
    vararg extras: Pair<String, Any?>,
    flags: Int? = null
) {
    requireActivity().openActivity(activity, *extras, flags = flags)
}

fun <T> Fragment.getNavigationResultLiveData(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.removeNavigationResultObserver(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.remove<T>(key)

fun <T> Fragment.setNavigationResult(result: T, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun Fragment.hideKeyboard() = hideSoftInput(requireActivity())