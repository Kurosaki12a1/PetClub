package com.kien.petclub.presentation.product.goods

import android.content.Context
import android.graphics.Color
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.databinding.FragmentGoodsBinding
import com.kien.petclub.domain.model.entity.ChooserItem
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.model.entity.getStock
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.navigateSafe
import com.kien.petclub.extensions.updateText
import com.kien.petclub.presentation.base.BaseFragment
import com.kien.petclub.presentation.home.HomeActivity
import com.kien.petclub.presentation.product.ProductListener
import com.kien.petclub.presentation.product.ShareMultiDataViewModel
import com.kien.petclub.presentation.product.SortProductListener
import com.kien.petclub.presentation.product.goods.GoodsAdapter.Companion.BUYING_PRICE
import com.kien.petclub.presentation.product.goods.GoodsAdapter.Companion.SELLING_PRICE
import com.kien.petclub.presentation.product.sort_product.SortChooserPopup
import com.kien.petclub.presentation.product.utils.showBottomNavigationAndFabButton
import com.kien.petclub.presentation.utils.PopupMenuHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GoodsFragment : BaseFragment<FragmentGoodsBinding>(), ProductListener, SortProductListener {
    private val viewModel: GoodsViewModel by activityViewModels()

    private val shareVM: ShareMultiDataViewModel by activityViewModels()

    private lateinit var adapter: GoodsAdapter
    override fun getViewBinding(): FragmentGoodsBinding =
        FragmentGoodsBinding.inflate(layoutInflater)

    override fun setUpViews() {
        adapter = GoodsAdapter(this)
        binding.rvProduct.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvProduct.adapter = adapter

        binding.filterPrice.updateText(viewModel.getFilterProduct())

        val filterPopup = PopupMenuHelper(
            requireActivity(),
            R.menu.filter_price_menu,
        ) { item ->
            when (item.itemId) {
                R.id.action_selling_price -> {
                    binding.filterPrice.text = getString(R.string.selling_price)
                    viewModel.setFilterProduct(getString(R.string.selling_price))
                    adapter.setFilterPrice(SELLING_PRICE)
                    true
                }

                R.id.action_buying_price -> {
                    binding.filterPrice.text = getString(R.string.buying_price)
                    viewModel.setFilterProduct(getString(R.string.buying_price))
                    adapter.setFilterPrice(BUYING_PRICE)
                    true
                }

                else -> false
            }
        }

        binding.filterPrice.setOnClickListener {
            filterPopup.show(it)
        }

        binding.ivSort.setOnClickListener {
            (requireActivity() as HomeActivity).showPopUpSort()
        }

        binding.ivSearch.setOnClickListener {
            (requireActivity() as HomeActivity).hideBottomNavigationAndFabButton()
            navigateSafe(GoodsFragmentDirections.actionOpenSearchFragment())
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllProduct()
        showBottomNavigationAndFabButton()
    }

    override fun setupObservers() {
        super.setupObservers()
        lifecycleScope.launch {
            viewModel.productResponse.flowWithLifecycle(lifecycle).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        adapter.setData(it.value, viewModel.getSortProduct())
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
            shareVM.setProduct(product)
            navigateSafe(GoodsFragmentDirections.actionOpenDetailFragment())
        }
    }

    override fun onSortClick(item: ChooserItem, position: Int) {
        adapter.sortData(position)
        viewModel.setSortProduct(position)
        (requireActivity() as HomeActivity).dismissPopUpSort()
    }

    private fun setInfoTotalProduct(list: ArrayList<Product>?) {
        if (list.isNullOrEmpty()) return
        val totalStock = list.sumOf { it.getStock() }
        val text = getString(R.string.goods_info, list.size.toString(), totalStock.toString())
        binding.infoGoods.text = viewModel.getSpannableStringAllProductText(
            list.size,
            totalStock.toString().length,
            text,
            resources.getColor(R.color.colorPrimary, null),
            Color.BLACK
        )
    }

}