package com.kien.petclub.domain.model.entity

import com.google.gson.annotations.SerializedName

data class Service(
    @SerializedName("MA_DICH_VU")
    val id: String,
    @SerializedName("MA_VACH_DICH_VU")
    val code: String?,
    @SerializedName("TEN_DICH_VU")
    val name: String,
    @SerializedName("NHOM_DICH_VU")
    val type: String,
    @SerializedName("THUONG_HIEU")
    val brands: String,
    @SerializedName("GIA_BAN") // Giá bán
    val sellingPrice: String,
    @SerializedName("GIA_VON") // Giá vốn
    val buyingPrice: String,
    @SerializedName("MO_TA")
    val description: String?,
    @SerializedName("GHI_CHU")
    val note: String?,
    @SerializedName("HINH_ANH")
    val photo: List<String>? = null
)