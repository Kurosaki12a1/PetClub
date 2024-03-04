package com.kien.petclub.presentation.product.goods

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.constants.Constants
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
import com.kien.petclub.presentation.product.SortProductListener
import com.kien.petclub.presentation.product.utils.showBottomNavigationAndFabButton
import com.kien.petclub.presentation.utils.PopupMenuHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GoodsFragment : BaseFragment<FragmentGoodsBinding>(), ProductListener, SortProductListener {
    private val viewModel: GoodsViewModel by activityViewModels()

    private lateinit var adapter: GoodsAdapter
    override fun getViewBinding(): FragmentGoodsBinding =
        FragmentGoodsBinding.inflate(layoutInflater)

    override fun setUpViews() {
        adapter = GoodsAdapter(this, viewModel.getFilterPrice() ?: "")
        binding.rvProduct.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvProduct.adapter = adapter

        binding.filterPrice.updateText(viewModel.getFilterPrice())

        val filterPopup = PopupMenuHelper(
            requireActivity(),
            R.menu.filter_price_menu,
            { item ->
                when (item.itemId) {
                    R.id.action_selling_price -> {
                        binding.filterPrice.text = getString(R.string.selling_price)
                        viewModel.setFilterPrice(getString(R.string.selling_price))
                        adapter.setFilterPrice(getString(R.string.selling_price))
                        true
                    }

                    R.id.action_buying_price -> {
                        binding.filterPrice.text = getString(R.string.buying_price)
                        viewModel.setFilterPrice(getString(R.string.buying_price))
                        adapter.setFilterPrice(getString(R.string.buying_price))
                        true
                    }

                    else -> false
                }
            })

        binding.filterPrice.setOnClickListener {
            filterPopup.show(it)
        }

        binding.ivFilter.setOnClickListener {
            navigateSafe(GoodsFragmentDirections.actionOpenFilterFragment())
        }

        binding.ivSort.setOnClickListener {
            (requireActivity() as HomeActivity).showPopUpSort()
        }

        binding.ivSearch.setOnClickListener {
            (requireActivity() as HomeActivity).hideBottomNavigationAndFabButton()
            navigateSafe(GoodsFragmentDirections.actionOpenSearchFragment())
        }

        binding.swipeRefreshProduct.setColorSchemeColors(Color.CYAN)

        binding.swipeRefreshProduct.setOnRefreshListener {
            viewModel.getAllProduct()
            binding.swipeRefreshProduct.isRefreshing = false
        }

        viewModel.getAllProduct()
    }

    override fun onResume() {
        super.onResume()
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
            setFragmentResult(Constants.KEY_PRODUCT, Bundle().apply {
                putParcelable(Constants.DATA, product)
            })
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
            list.size.toString().length,
            totalStock.toString().length,
            text,
            resources.getColor(R.color.colorPrimary, null),
            Color.BLACK
        )
    }

}