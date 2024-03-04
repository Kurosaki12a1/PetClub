package com.kien.petclub.presentation.product.search

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.kien.petclub.data.data_source.local.preferences.AppPreferences
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchProductViewModel @Inject constructor(
    private val pref: AppPreferences
) : BaseViewModel() {

    fun getSpannableStringAllProductText(
        numOfProducts: Int,
        numOfTotalStock: Int,
        textDisplay: String,
        highlightColor: Int,
        normalColor: Int
    ): SpannableString {
        val spannable = SpannableString(textDisplay)

        val colorHighLightTextProduct = ForegroundColorSpan(highlightColor)
        val colorHighLightTextStock = ForegroundColorSpan(normalColor)
        val colorNormalText = ForegroundColorSpan(Color.BLACK)

        spannable.setSpan(
            colorHighLightTextProduct,
            0,
            numOfProducts,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannable.setSpan(
            colorNormalText,
            numOfProducts + 1,
            textDisplay.length - numOfTotalStock,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            colorHighLightTextStock,
            textDisplay.length - numOfTotalStock,
            textDisplay.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    fun getSortProduct() = pref.productSort

    fun setSortProduct(sort: Int) {
        pref.productSort = sort
    }

    fun getFilterPrice() = pref.priceFilter

    fun setFilterPrice(filter: String) {
        pref.priceFilter = filter
    }
}