package com.kien.petclub.presentation.utils

import android.content.Context
import android.view.View
import android.widget.PopupMenu

class PopupMenuHelper(
    private val context: Context,
    private val menuId: Int,
    private val onMenuItemClickListener: PopupMenu.OnMenuItemClickListener
) {

    fun show(v: View, action: (PopupMenu.() -> Unit) = {}) {
        val popupMenu = PopupMenu(context, v)
        popupMenu.menuInflater.inflate(menuId, popupMenu.menu)
        action(popupMenu)
        popupMenu.setOnMenuItemClickListener(onMenuItemClickListener)
        popupMenu.show()
    }
}