package com.kien.petclub.presentation.add_info_product

import com.kien.petclub.domain.model.entity.InfoProduct

interface SearchInfoProductListener {
    fun onAddInfoProduct(data: InfoProduct)

    fun onDeleteInfoProduct(data: InfoProduct)

    fun onClickListener(data: InfoProduct)
}