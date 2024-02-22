package com.kien.petclub.presentation.product.common

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
    fun onImagePickerClick(position: Int) {}
    fun onImageDeleteClick(position: Int) {}
}