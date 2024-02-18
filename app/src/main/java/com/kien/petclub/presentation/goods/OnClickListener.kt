package com.kien.petclub.presentation.goods

import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.presentation.goods.popup.ChooserItem

interface OnClickListener {
    fun onItemClick(product: Product) {}

    fun onItemClick(item: ChooserItem, position: Int) {}
}