package com.kien.petclub.domain.model.entity

data class InfoProduct(
    var id: String = "",
    var name: String = "",
    var parentId : String? = null,
    var child: ArrayList<InfoProduct>? = null
)