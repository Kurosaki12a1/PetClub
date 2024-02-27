package com.kien.petclub.extensions

import android.graphics.Rect
import android.view.View
import android.widget.EditText

fun View.getVisibleRect(): Rect {
    val outRect = Rect()
    getGlobalVisibleRect(outRect)
    return outRect
}

fun EditText.updateText(text: String?) {
    if (text.isNullOrBlank() || text.isEmpty()) this.setText("")
    setText(text)
}

fun EditText.setOnDrawableRightClick(onClick: () -> Unit) {
    setOnTouchListener { et, event ->
        if (event.action == android.view.MotionEvent.ACTION_UP) {
            val touchPoint = event.rawX
            val compoundDrawablesRight = compoundDrawables[2]
            compoundDrawablesRight?.let {
                val drawableRightWidth = it.bounds.width()
                val etPaddingRight = et.paddingRight
                val drawableRightStart = et.width - etPaddingRight - drawableRightWidth
                if (touchPoint >= drawableRightStart) {
                    onClick.invoke()
                    return@setOnTouchListener true
                }
            }
        }
        performClick()
        return@setOnTouchListener false
    }
}

fun EditText.setOnDrawableLeftClick(onClick: () -> Unit) {
    setOnTouchListener { et, event ->
        if (event.action == android.view.MotionEvent.ACTION_UP) {
            val touchPoint = event.rawX
            val compoundDrawablesRight = compoundDrawables[0]
            compoundDrawablesRight?.let {
                val drawableLeftWidth = it.bounds.width()
                val etPaddingLeft = et.paddingLeft
                if (touchPoint <= drawableLeftWidth + etPaddingLeft) {
                    onClick.invoke()
                    return@setOnTouchListener true
                }
            }
        }
        performClick()
        return@setOnTouchListener false
    }
}