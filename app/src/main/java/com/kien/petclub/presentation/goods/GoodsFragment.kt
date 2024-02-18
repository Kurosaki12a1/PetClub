package com.kien.petclub.presentation.goods

import android.content.Context
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
import com.kien.petclub.presentation.goods.popup.ChooserItem
import com.kien.petclub.presentation.goods.popup.SortChooserPopup
import com.kien.petclub.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GoodsFragment : BaseFragment<FragmentGoodsBinding>(), OnClickListener {

    companion object {
        private const val TAG_POPUP = "SortChooserPopup"
    }

    private lateinit var popUpSort: SortChooserPopup

    private val viewModel: GoodsViewModel by viewModels()

    private lateinit var adapter: GoodsAdapter
    override fun getViewBinding(): FragmentGoodsBinding =
        FragmentGoodsBinding.inflate(layoutInflater)

    override fun setUpViews() {
        adapter = GoodsAdapter(this)
        binding.rvProduct.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvProduct.adapter = adapter

        popUpSort = SortChooserPopup(initChooserItem(requireActivity()),this)

        binding.filterPrice.setOnClickListener {
            showPopup(it)
        }

        binding.ivSort.setOnClickListener {
            if (!popUpSort.isVisible) {
                popUpSort.show(requireActivity().supportFragmentManager, TAG_POPUP)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllProduct()
    }

    override fun setupObservers() {
        super.setupObservers()
        // OnResume fragment only called when fragment recreated, so we must cal this

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

    override fun onItemClick(item: ChooserItem, position: Int) {
        popUpSort.dismiss()
    }

    private fun setInfoTotalProduct(list: ArrayList<Product>?) {
        if (list.isNullOrEmpty()) return
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

    private fun initChooserItem(context: Context): ArrayList<ChooserItem> {
        return arrayListOf(
            ChooserItem(context.getString(R.string.newest), true),
            ChooserItem(context.getString(R.string.oldest), false),
            ChooserItem(context.getString(R.string.name_az), false),
            ChooserItem(context.getString(R.string.name_za), false),
            ChooserItem(context.getString(R.string.price_low_high), false),
            ChooserItem(context.getString(R.string.price_high_low), false),
            ChooserItem(context.getString(R.string.inventory_low_high), false),
            ChooserItem(context.getString(R.string.inventory_high_low), false),
            ChooserItem(context.getString(R.string.sell_low_height), false),
            ChooserItem(context.getString(R.string.sell_high_low), false)
        )
    }

}