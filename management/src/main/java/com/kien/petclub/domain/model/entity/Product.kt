package com.kien.petclub.domain.model.entity

import android.os.Parcelable
import com.google.firebase.database.DataSnapshot
import kotlinx.parcelize.Parcelize
@Parcelize
sealed class Product : Parcelable {
    @Parcelize
    data class Service(
        val id: String,
        val code: String? = "",
        val name: String,
        val type: String? = null,
        val brands: String? = null,
        val sellingPrice: String,
        val buyingPrice: String,
        val description: String?,
        val note: String?,
        var photo: List<String>? = null,
        val updatedDate: String? = null
    ) : Product(), Parcelable

    @Parcelize
    data class Goods(
        val id: String,
        val code: String? = "",
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
        var photo: List<String>? = null,
        val minimumStock: String? = "0",
        val maximumStock: String? = "999999999",
        val updatedDate: String? = null
    ) : Product(), Parcelable
}

fun Product.getPhoto(): List<String>? {
    return when (this) {
        is Product.Goods -> this.photo
        is Product.Service -> this.photo
    }
}

fun Product.deletePhoto(position: Int) {
    when (this) {
        is Product.Goods -> {
            val myPhoto = photo?.toMutableList()
            myPhoto?.removeAt(position)
            this.photo = myPhoto
        }

        is Product.Service -> {
            val myPhoto = photo?.toMutableList()
            myPhoto?.removeAt(position)
            this.photo = myPhoto
        }
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

fun Product.getCode(): String {
    return when (this) {
        is Product.Goods -> this.code ?: ""
        is Product.Service -> this.code ?: ""
    }
}

fun Product.getUpdateDated(): Long {
    return when (this) {
        is Product.Goods -> this.updatedDate?.toLong() ?: System.currentTimeMillis()
        is Product.Service -> this.updatedDate?.toLong() ?: System.currentTimeMillis()
    }
}

fun Product.getBuyingPrice(): Double {
    return when (this) {
        is Product.Goods -> this.buyingPrice.toDouble()
        is Product.Service -> this.buyingPrice.toDouble()
    }
}

fun Product.getSellingPrice(): Double {
    return when (this) {
        is Product.Goods -> this.sellingPrice.toDouble()
        is Product.Service -> this.sellingPrice.toDouble()
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
    val updatedDate = snapshot.child("updatedDate").getValue(String::class.java)

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
    val updatedDate = snapshot.child("updatedDate").getValue(String::class.java)

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

enum class ProductSortType(val value: Int, val description: String) {
    NEWEST(0, "Newest"),
    OLDEST(1, "Oldest"),
    NAME_AZ(2, "Name A-Z"),
    NAME_ZA(3, "Name Z-A"),
    PRICE_LOW_TO_HIGH(4, "Price Low to High"),
    PRICE_HIGH_TO_LOW(5, "Price High to Low"),
    INVENTORY_LOW_TO_HIGH(6, "Inventory Low to High"),
    INVENTORY_HIGH_TO_LOW(7, "Inventory High to Low"),
    SELL_LOW_TO_HIGH(8, "Sell Low to High"),
    SELL_HIGH_TO_LOW(9, "Sell High to Low");

    companion object {

        fun initListChooser(): ArrayList<ChooserItem> {
            return entries.mapIndexed { index, it ->
                ChooserItem(it.description, index == 0)
            }.toCollection(ArrayList())
        }

        fun getListSort(
            list: ArrayList<Product>,
            sort: Int,
            isSortedSellingPrice: Boolean = true
        ): ArrayList<Product> {
            return when (sort) {
                NEWEST.value -> {
                    list.sortedByDescending { it.getUpdateDated() }
                }

                OLDEST.value -> {
                    list.sortedBy { it.getUpdateDated() }
                }

                NAME_AZ.value -> {
                    list.sortedBy { it.getName() }
                }

                NAME_ZA.value -> {
                    list.sortedByDescending { it.getName() }
                }

                PRICE_LOW_TO_HIGH.value -> {
                    list.sortedBy { if (isSortedSellingPrice) it.getSellingPrice() else it.getBuyingPrice() }
                }

                PRICE_HIGH_TO_LOW.value -> {
                    list.sortedByDescending { if (isSortedSellingPrice) it.getSellingPrice() else it.getBuyingPrice() }
                }

                INVENTORY_LOW_TO_HIGH.value -> {
                    list.sortedBy { it.getStock() }
                }

                INVENTORY_HIGH_TO_LOW.value -> {
                    list.sortedByDescending { it.getStock() }
                }

                SELL_LOW_TO_HIGH.value -> {
                    list.sortedBy { it.getSellingPrice() }
                }

                SELL_HIGH_TO_LOW.value -> {
                    list.sortedByDescending { it.getSellingPrice() }
                }

                else -> list
            }.toCollection(ArrayList())
        }
    }

}
