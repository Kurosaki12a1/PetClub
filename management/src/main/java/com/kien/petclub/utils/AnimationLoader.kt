package com.kien.petclub.utils

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.kien.petclub.R
import javax.inject.Inject

class AnimationLoader @Inject constructor(private val context: Context) {
    val fromBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.from_bottom_fab)
    }
    val toBottomFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.to_bottom_fab)
    }
    val rotateClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.rotate_clock_wise)
    }
    val rotateAntiClockWiseFabAnim: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.rotate_anti_clock_wise)
    }
    val fromBottomBgAnim: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.from_bottom_anim)
    }
    val toBottomBgAnim: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.to_bottom_anim)
    }
}