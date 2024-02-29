package com.kien.petclub.constants

object Constants {
    // Hàng hóa
    const val GOODS_DB = "DU_LIEU_HANG_HOA"
//    const val GOODS_ID = "MA_HANG_HOA"
//    const val GOODS_CODE = "MA_VACH_HANG_HOA"
//    const val GOODS_NAME = "TEN_HANG_HOA"
//    const val GOODS_TYPE = "NHOM_HANG_HOA"
//    const val GOODS_BRAND = "THUONG_HIEU"
//    const val GOODS_SELLING_PRICE = "GIA_BAN"
//    const val GOODS_COST_PRICE = "GIA_VON"
//    const val GOODS_EXIST = "TON_KHO"
//    const val GOODS_WEIGHT = "TRONG_LUONG"
//    const val GOODS_LOCATION = "VI_TRI"
//    const val GOODS_DESCRIPTION = "MO_TA"
//    const val GOODS_NOTE = "GHI_CHU"
//    const val GOODS_PHOTO = "HINH_ANH"

    // Dịch vụ
    const val SERVICE_DB = "DU_LIEU_DICH_VU"
//    const val SERVICE_ID = "MA_DICH_VU"
//    const val SERVICE_CODE = "MA_VACH_DICH_VU"
//    const val SERVICE_NAME = "TEN_DICH_VU"
//    const val SERVICE_TYPE = "NHOM_DICH_VU"
//    const val SERVICE_BRAND = "THUONG_HIEU"
//    const val SERVICE_SELLING_PRICE = "GIA_BAN"
//    const val SERVICE_COST_PRICE = "GIA_VON"
//    const val SERVICE_DESCRIPTION = "MO_TA"
//    const val SERVICE_NOTE = "GHI_CHU"
//    const val SERVICE_PHOTO = "HINH_ANH"
//
//    // Notifications
//    const val NOTIFICATION_DB = "THONG_BAO"
//    const val NOTIFICATION_DESCRIPTION = "NOI_DUNG"
//    const val NOTIFICATION_TIME = "THOI_GIAN"
//    const val NOTIFICATIONS_ICON = "HINH_ANH"

    const val TYPE_DB = "ALL_TYPE_PRODUCT"
    const val BRAND_DB = "ALL_BRAND_PRODUCT"
    const val LOCATION_DB = "ALL_LOCATION_PRODUCT"

    // User
    const val USER_DB = "DU_LIEU_NGUOI_DUNG"

    // Type of products
    const val KEY_TYPE = "key_type"
    const val VALUE_GOODS = "goods"
    const val VALUE_SERVICE = "service"
    const val VALUE_BRAND = "brand"
    const val VALUE_TYPE = "type"
    const val VALUE_LOCATION = "location"
    const val DATA = "data"

    const val TIMEOUT_BACK_PRESS = 2000L
    const val EMPTY_STRING = ""

    // Product Fragment
    const val TAG_ADD_INFO_PRODUCT_POPUP = "AddInfoProductPopUp"

    // UseCase
    const val EMPTY_EMAIL = "Email is empty"
    const val INVALID_EMAIL = "Email format is invalid."
    const val EMPTY_PASSWORD = "Password is empty"
    const val INVALID_PASSWORD =
        "Password must be at least 6 characters long and contain at least one uppercase letter"
    const val PASSWORD_NOT_MATCH = "Password and re-password are not the same"
    const val EMPTY_NAME = "Name is empty"
    const val EMPTY_PHONE = "Phone is empty"

    const val EXISTED_PRODUCT = "Product id already exists"


    // SharedPreferences
    const val KEY_FILTER = "key_filter"
    const val SORT_FILTER = "sort_filter"
}