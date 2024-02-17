package com.kien.petclub.presentation.goods

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.databinding.FragmentGoodsBinding
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.model.entity.getStock
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseFragment
import com.kien.petclub.presentation.goods.GoodsAdapter.Companion.BUYING_PRICE
import com.kien.petclub.presentation.goods.GoodsAdapter.Companion.SELLING_PRICE
import com.kien.petclub.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GoodsFragment : BaseFragment<FragmentGoodsBinding>(), GoodsListener {

    private val viewModel: GoodsViewModel by viewModels()

    private lateinit var adapter: GoodsAdapter
    override fun getViewBinding(): FragmentGoodsBinding =
        FragmentGoodsBinding.inflate(layoutInflater)

    override fun setUpViews() {
        adapter = GoodsAdapter(this)
        binding.rvProduct.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvProduct.adapter = adapter

        binding.filterPrice.setOnClickListener {
            showPopup(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllProduct()
    }

    override fun setupObservers() {
        super.setupObservers()
        lifecycleScope.launch {
            viewModel.productResponse.collect {
                when (it) {
                    is Resource.Success -> {
                        adapter.setData(it.value)
                        setInfoTotalProduct(it.value)
                        (requireActivity() as HomeActivity).stopLoadingAnimation()
                    }

                    is Resource.Loading -> {
                        (requireActivity() as HomeActivity).showLoadingAnimation()
                    }

                    is Resource.Failure -> {
                        (requireActivity() as HomeActivity).stopLoadingAnimation()
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onItemClick(product: Product) {
        lifecycleScope.launch {
            val act = requireActivity() as HomeActivity
            act.setDataToSharedVM(product)
            (requireActivity() as HomeActivity).navigateToDetailProduct()
        }
    }

    private fun setInfoTotalProduct(list: ArrayList<Product>) {
        val totalStock = list.sumOf { it.getStock() }.toString()
        val text = getString(R.string.goods_info, list.size.toString(), totalStock)
        val spannable = SpannableString(text)
        val colorHighLightTextProduct =
            ForegroundColorSpan(resources.getColor(R.color.colorPrimary, null))
        val colorHighLightTextStock =
            ForegroundColorSpan(resources.getColor(R.color.colorPrimary, null))
        val colorNormalText = ForegroundColorSpan(Color.BLACK)
        spannable.setSpan(
            colorHighLightTextProduct,
            0,
            list.size.toString().length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            colorNormalText,
            list.size.toString().length + 1,
            text.length - totalStock.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            colorHighLightTextStock,
            text.length - totalStock.length,
            text.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.infoGoods.text = spannable
    }

    private fun showPopup(v: View) {
        val popUp = PopupMenu(v.context, v)
        val inflater = popUp.menuInflater
        inflater.inflate(R.menu.filter_price_menu, popUp.menu)
        popUp.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_selling_price -> {
                    binding.filterPrice.text = getString(R.string.selling_price)
                    adapter.setFilterPrice(SELLING_PRICE)
                    true
                }

                R.id.action_buying_price -> {
                    binding.filterPrice.text = getString(R.string.buying_price)
                    adapter.setFilterPrice(BUYING_PRICE)
                    true
                }

                else -> false
            }
        }
        popUp.show()
    }


}