package com.kien.imagepicker.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.graphics.Rect
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
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

/**
 *  Expand the recycler view inside the item view
 */
fun RecyclerView.expandItemView(height: Int, view: ImageView, duration: Long = 300L) {
    val valueAnimator = ValueAnimator.ofInt(0, height).apply {
        this.duration = duration
        addUpdateListener { animation ->
            interpolator = LinearInterpolator()
            layoutParams.height = animation.animatedValue as Int
            layoutParams = layoutParams
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                visibility = View.VISIBLE
                view.setImageResource(R.drawable.ic_collapse)
            }
        })
    }
    valueAnimator.start()
}

fun RecyclerView.collapseItemView(view: ImageView, duration: Long = 300L) {
    val initialHeight = measuredHeight
    val valueAnimator = ValueAnimator.ofInt(initialHeight, 0).apply {
        this.duration = duration
        addUpdateListener { animation ->
            interpolator = LinearInterpolator()
            layoutParams.height = animation.animatedValue as Int
            layoutParams = layoutParams
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.setImageResource(R.drawable.ic_add)
                visibility = View.GONE
            }
        })
    }
    valueAnimator.start()
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