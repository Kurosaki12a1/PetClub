package com.kien.petclub.data.mapper

import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.model.entity.InfoProductItem

fun InfoProduct.toInfoProductItem(): InfoProductItem = InfoProductItem(
    id = id,
    name = name,
    fullName = fullName,
    parentId = parentId,
    child = child?.map { it.toInfoProductItem() }?.toCollection(ArrayList()),
    isSelected = false
)