package com.kien.petclub.extensions

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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

fun Fragment.checkAndRequestPermission(permission: String, requestCode: Int) {
    requireActivity().checkAndRequestPermission(permission, requestCode)
}

fun Fragment.requestPermissionLauncher(
    onResult: (Boolean) -> Unit
): ActivityResultLauncher<String> {
    val launcher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            onResult(isGranted)
        }
    return launcher
}


fun Fragment.getResultLauncher(
    onResult: (Int, Intent?) -> Unit
): ActivityResultLauncher<Intent> {
    val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onResult(result.resultCode, result.data)
        }
    return launcher
}