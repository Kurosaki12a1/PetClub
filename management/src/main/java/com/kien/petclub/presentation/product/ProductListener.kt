package com.kien.petclub.presentation.product

import com.kien.petclub.domain.model.entity.ChooserItem
import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.model.entity.Product

interface ProductListener {
    fun onItemClick(product: Product) {}

    fun onItemClick(item: ChooserItem, position: Int) {}

    fun onAddInfoProduct(data: InfoProduct) {}

    fun onDeleteInfoProduct(data: InfoProduct) {}

    fun onClickListener(data: InfoProduct) {}
}

interface ImagePickerListener {
    fun onTakePhotoClick() {}
}