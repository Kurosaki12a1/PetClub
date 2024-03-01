package com.kien.petclub.presentation.product.goods

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.lifecycle.viewModelScope
import com.kien.petclub.data.data_source.local.preferences.AppPreferences
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.usecase.firebase_db.product.GetProductUseCase
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoodsViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val pref : AppPreferences
) : BaseViewModel() {

    private val _productResponse =
        MutableStateFlow<Resource<ArrayList<Product>>>(Resource.Default)
    val productResponse = _productResponse.asStateFlow()

    fun getAllProduct() {
        viewModelScope.launch {
            getProductUseCase.getGoodsUseCase().flatMapMerge { goods ->
                getProductUseCase.getServicesUseCase().map { service ->
                    when {
                        goods is Resource.Success && service is Resource.Success -> {
                            val list = ArrayList<Product>()
                            list.addAll(goods.value)
                            list.addAll(service.value)
                            Resource.success(list)
                        }

                        goods is Resource.Failure -> {
                            Resource.failure(goods.error, goods.errorMessage)
                        }

                        service is Resource.Failure -> {
                            Resource.failure(service.error, service.errorMessage)
                        }

                        else -> Resource.Loading
                    }
                }
            }.collect {
                _productResponse.value = it
            }
        }
    }

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
            numOfProducts,
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

    fun getFilterProduct() = pref.productFilter

    fun setFilterProduct(filter: String) {
        pref.productFilter = filter
    }
}