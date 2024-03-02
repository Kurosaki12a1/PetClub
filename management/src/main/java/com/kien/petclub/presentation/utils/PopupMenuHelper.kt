package com.kien.petclub.presentation.utils

import android.content.Context
import android.view.View
import android.widget.PopupMenu

class PopupMenuHelper(
    private val context: Context,
    private val menuId: Int,
    private val onMenuItemClickListener: PopupMenu.OnMenuItemClickListener,
    val action: (PopupMenu.() -> Unit) = {}
) {

    fun show(v: View) {
        val popupMenu = PopupMenu(context, v)
        popupMenu.menuInflater.inflate(menuId, popupMenu.menu)
        action(popupMenu)
        popupMenu.setOnMenuItemClickListener(onMenuItemClickListener)
        popupMenu.show()
    }
}