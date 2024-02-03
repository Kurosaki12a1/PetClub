package com.kien.petclub.extensions

import android.graphics.Rect
import android.view.View

fun View.getVisibleRect(): Rect {
    val outRect = Rect()
    getGlobalVisibleRect(outRect)
    return outRect
}

fun View.isInVisibleRect(x: Int, y: Int): Boolean {
    val outRect = getVisibleRect()
    return outRect.contains(x, y)
}