package com.kien.petclub.presentation.goods

import com.kien.petclub.domain.model.entity.Product

interface GoodsListener {
    fun onItemClick(product : Product)
}