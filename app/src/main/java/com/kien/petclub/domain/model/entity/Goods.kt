package com.kien.petclub.domain.model.entity

import com.google.gson.annotations.SerializedName

data class Goods(
    @SerializedName("MA_HANG_HOA")
    val id: String,
    @SerializedName("MA_VACH_HANG_HOA")
    val code: String,
    @SerializedName("TEN_HANG_HOA")
    val name: String,
    @SerializedName("NHOM_HANG_HOA")
    val type: String? = null,
    @SerializedName("THUONG_HIEU")
    val brands: String? = null,
    @SerializedName("GIA_BAN") // Giá bán
    val sellingPrice: String,
    @SerializedName("GIA_VON") // Giá vốn
    val buyingPrice: String,
    @SerializedName("TON_KHO")
    val stock: String,
    @SerializedName("TRONG_LUONG")
    val weight: String,
    @SerializedName("VI_TRI")
    val location: String? = null,
    @SerializedName("MO_TA")
    val description: String,
    @SerializedName("GHI_CHU")
    val note: String,
    @SerializedName("HINH_ANH")
    val photo: List<String>? = null
)