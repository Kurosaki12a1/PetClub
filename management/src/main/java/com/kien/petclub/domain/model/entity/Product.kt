package com.kien.petclub.domain.model.entity

import com.google.firebase.database.DataSnapshot

sealed class Product {
    data class Service(
        val id: String,
        val code: String?,
        val name: String,
        val type: String? = null,
        val brands: String? = null,
        val sellingPrice: String,
        val buyingPrice: String,
        val description: String?,
        val note: String?,
        val photo: List<String>? = null,
        val updatedDate: Long? = null
    ) : Product()

    data class Goods(
        val id: String,
        val code: String,
        val name: String,
        val type: String? = null,
        val brands: String? = null,
        val sellingPrice: String,
        val buyingPrice: String,
        val stock: String,
        val weight: String,
        val location: String? = null,
        val description: String,
        val note: String,
        val photo: List<String>? = null,
        val minimumStock: String? = "0",
        val maximumStock: String? = "999999999",
        val updatedDate: Long? = null
    ) : Product()
}

fun Product.getPhoto(): List<String>? {
    return when (this) {
        is Product.Goods -> this.photo
        is Product.Service -> this.photo
    }
}

fun Product.getStock(): Int {
    return when (this) {
        is Product.Goods -> this.stock.toInt()
        is Product.Service -> 0
    }
}

fun Product.getName(): String {
    return when (this) {
        is Product.Goods -> this.name
        is Product.Service -> this.name
    }
}

fun Product.getId(): String {
    return when (this) {
        is Product.Goods -> this.id
        is Product.Service -> this.id
    }
}

fun Product.getUpdateDated(): Long? {
    return when (this) {
        is Product.Goods -> this.updatedDate
        is Product.Service -> this.updatedDate
    }
}

fun Product.getBuyingPrice(): Long {
    return when (this) {
        is Product.Goods -> this.buyingPrice.toLong()
        is Product.Service -> this.buyingPrice.toLong()
    }
}

fun Product.getSellingPrice(): Long {
    return when (this) {
        is Product.Goods -> this.sellingPrice.toLong()
        is Product.Service -> this.sellingPrice.toLong()
    }
}

fun mapSnapshotToGoods(snapshot: DataSnapshot): Product.Goods {
    val id = snapshot.child("id").getValue(String::class.java)
    val code = snapshot.child("code").getValue(String::class.java)
    val name = snapshot.child("name").getValue(String::class.java)
    val type = snapshot.child("type").getValue(String::class.java)
    val brands = snapshot.child("brands").getValue(String::class.java)
    val sellingPrice = snapshot.child("sellingPrice").getValue(String::class.java)
    val buyingPrice = snapshot.child("buyingPrice").getValue(String::class.java)
    val stock = snapshot.child("stock").getValue(String::class.java)
    val weight = snapshot.child("weight").getValue(String::class.java)
    val location = snapshot.child("location").getValue(String::class.java)
    val description = snapshot.child("description").getValue(String::class.java)
    val note = snapshot.child("note").getValue(String::class.java)
    val minimumStock = snapshot.child("minimumStock").getValue(String::class.java)
    val maximumStock = snapshot.child("maximumStock").getValue(String::class.java)
    val updatedDate = snapshot.child("updatedDate").getValue(Long::class.java)

    val photo = snapshot.child("photo").children.mapNotNull { photoSnapshot ->
        photoSnapshot.getValue(String::class.java)
    }.let {
        if (it.isEmpty()) null else ArrayList(it)
    }
    return Product.Goods(
        id ?: "",
        code ?: "",
        name ?: "",
        type,
        brands,
        sellingPrice ?: "",
        buyingPrice ?: "",
        stock ?: "",
        weight ?: "",
        location,
        description ?: "",
        note ?: "",
        photo,
        minimumStock,
        maximumStock,
        updatedDate
    )
}

fun mapSnapshotToService(snapshot: DataSnapshot): Product.Service {
    val id = snapshot.child("id").getValue(String::class.java)
    val code = snapshot.child("code").getValue(String::class.java)
    val name = snapshot.child("name").getValue(String::class.java)
    val type = snapshot.child("type").getValue(String::class.java)
    val brands = snapshot.child("brands").getValue(String::class.java)
    val sellingPrice = snapshot.child("sellingPrice").getValue(String::class.java)
    val buyingPrice = snapshot.child("buyingPrice").getValue(String::class.java)
    val description = snapshot.child("description").getValue(String::class.java)
    val note = snapshot.child("note").getValue(String::class.java)
    val updatedDate = snapshot.child("updatedDate").getValue(Long::class.java)

    val photo = snapshot.child("photo").children.mapNotNull { photoSnapshot ->
        photoSnapshot.getValue(String::class.java)
    }.let {
        if (it.isEmpty()) null else ArrayList(it)
    }
    return Product.Service(
        id ?: "",
        code,
        name ?: "",
        type,
        brands,
        sellingPrice ?: "",
        buyingPrice ?: "",
        description ?: "",
        note ?: "",
        photo,
        updatedDate
    )
}