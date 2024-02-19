package com.kien.petclub

import com.kien.petclub.domain.model.entity.Product
import java.lang.ref.WeakReference

// Shared data whole application
object DataHolder {
    private const val TAG = "DATA_HOLDER"

    private val data: HashMap<String, WeakReference<Product>> = HashMap()

    fun put(product: Product) {
        data[TAG] = WeakReference(product)
    }

    fun retrieve(): Product? {
        return data[TAG]?.get()
    }
}