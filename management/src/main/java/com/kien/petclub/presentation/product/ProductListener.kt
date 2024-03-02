package com.kien.petclub.presentation.product

import android.net.Uri
import com.kien.petclub.domain.model.entity.ChooserItem
import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.model.entity.Product

interface ProductListener {
    fun onItemClick(product: Product) {}
}

interface InfoProductListener {
    fun onAddInfoProduct(infoName : String) {}
    fun onAddSubInfoProduct(data: InfoProduct) {}

    fun onDeleteInfoProduct(data: InfoProduct) {}

    fun onClickListener(data: InfoProduct) {}
}

interface SortProductListener {
    fun onSortClick(item: ChooserItem, position: Int)
}

interface ImagePickerListener {
    fun onTakePhotoClick() {}

    fun onDeletePhoto(uri: Uri, position: Int) {}
}