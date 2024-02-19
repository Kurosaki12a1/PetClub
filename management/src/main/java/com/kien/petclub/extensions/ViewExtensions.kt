package com.kien.petclub.extensions

import android.graphics.Rect
import android.view.View
import android.widget.EditText

fun View.getVisibleRect(): Rect {
    val outRect = Rect()
    getGlobalVisibleRect(outRect)
    return outRect
}

fun View.isInVisibleRect(x: Int, y: Int): Boolean {
    val outRect = getVisibleRect()
    return outRect.contains(x, y)
}

fun EditText.updateText(text: String?) {
    if (text.isNullOrBlank() || text.isEmpty()) this.setText("")
    setText(text)
}