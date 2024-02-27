package com.kien.imagepicker.extensions

import android.graphics.Rect
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.kien.imagepicker.R

fun RecyclerView.slideUpAnimation() {
    val slideUpAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_in)
    slideUpAnimation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {
            visibility = View.VISIBLE
        }

        override fun onAnimationEnd(p0: Animation?) {
        }

        override fun onAnimationRepeat(p0: Animation?) {
        }

    })
    startAnimation(slideUpAnimation)
}

fun RecyclerView.slideDownAnimation() {
    val slideDownAnimation = AnimationUtils.loadAnimation(context, R.anim.anim_out)
    slideDownAnimation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(p0: Animation?) {}

        override fun onAnimationEnd(p0: Animation?) {
            visibility = View.GONE
        }

        override fun onAnimationRepeat(p0: Animation?) {}
    })
    startAnimation(slideDownAnimation)
}

fun View.getVisibleRect(): Rect {
    val outRect = Rect()
    getGlobalVisibleRect(outRect)
    return outRect
}

fun View.isInVisibleRect(x: Int, y: Int): Boolean {
    val outRect = getVisibleRect()
    return outRect.contains(x, y)
}