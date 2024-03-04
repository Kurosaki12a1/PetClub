package com.kien.petclub.data.data_source.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.kien.petclub.R
import javax.inject.Inject

class AppPreferences @Inject constructor(val context: Context) {
    companion object {
        private const val APP_PREFERENCES_NAME = "PET-CLUB-Cache"
        private const val MODE = Context.MODE_PRIVATE

        private const val PRICE_FILTER = "priceFilter"
        private const val PRODUCT_FILTER = "productFilter"
        private const val PRODUCT_SORT = "productSort"
    }

    private val appPreferences: SharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES_NAME, MODE)


    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    fun putString(key: String, value: String) = appPreferences.edit {
        it.putString(key, value)
    }

    fun putBoolean(key: String, value: Boolean) = appPreferences.edit {
        it.putBoolean(key, value)
    }

    fun getString(key: String, defaultValue: String = ""): String? =
        appPreferences.getString(key, defaultValue)

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean =
        appPreferences.getBoolean(key, defaultValue)

    var priceFilter: String?
        get() = appPreferences.getString(PRICE_FILTER, context.getString(R.string.selling_price))
        set(value) = appPreferences.edit {
            it.putString(PRICE_FILTER, value)
        }

    var productSort: Int
        get() = appPreferences.getInt(PRODUCT_SORT, 0)
        set(value) = appPreferences.edit {
            it.putInt(PRODUCT_SORT, value)
        }

    fun clearPreferences() {
        appPreferences.edit {
            it.clear().apply()
        }
    }
}