package com.kien.imagepicker.extensions

import android.app.Activity
import android.os.Build
import com.kien.imagepicker.R

/**
 * Initializes and applies a "close" transition effect for the activity.
 *
 * This function configures the exit and entrance animation for closing the current activity. It checks the
 * Android version of the device and applies the appropriate transition effect based on the version.
 * For devices running Android UPSIDE_DOWN_CAKE (a placeholder for an Android version) or later, a specific
 * method `overrideActivityTransition` is called. For older versions, `overridePendingTransition` is used instead.
 * The animations for sliding in from the left and sliding out to the right are applied, giving a natural "closing"
 * effect to the activity transition.
 * @author Thinh Huynh
 * @since 27/02/2024
 */
fun Activity.initTransitionClose() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        overrideActivityTransition(
            Activity.OVERRIDE_TRANSITION_CLOSE,
            R.anim.anim_slide_in_left,
            R.anim.anim_slide_out_right
        )
    } else {
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
    }
    finish()
}


/**
 * Initializes and applies an "open" transition effect for the activity.
 *
 * Similar to `initTransitionClose`, this function sets up the entrance and exit animations for opening
 * a new activity. It determines the correct transition method to use based on the Android version of the device.
 * For newer versions (Android UPSIDE_DOWN_CAKE or later), it uses `overrideActivityTransition` with specific
 * transition types. For older versions, `overridePendingTransition` is utilized. The chosen animations slide
 * in from the right and slide out to the left, creating a seamless "opening" effect for the activity transition.
 * @author Thinh Huynh
 * @since 27/02/2024
 */
fun Activity.initTransitionOpen() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        overrideActivityTransition(
            Activity.OVERRIDE_TRANSITION_OPEN,
            R.anim.anim_slide_in_right,
            R.anim.anim_slide_out_left
        )
    } else {
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left)
    }
}