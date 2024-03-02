package com.kien.petclub.presentation.product.search

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.constants.Constants
import com.kien.petclub.databinding.FragmentSearchProductBinding
import com.kien.petclub.domain.model.entity.ChooserItem
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.model.entity.getCode
import com.kien.petclub.domain.model.entity.getId
import com.kien.petclub.domain.model.entity.getName
import com.kien.petclub.domain.model.entity.getStock
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.extensions.navigateSafe
import com.kien.petclub.extensions.setOnDrawableRightClick
import com.kien.petclub.extensions.updateText
import com.kien.petclub.presentation.home.HomeActivity
import com.kien.petclub.presentation.product.ProductListener
import com.kien.petclub.presentation.product.SortProductListener
import com.kien.petclub.presentation.product.base.BarcodeFragment
import com.kien.petclub.presentation.product.goods.GoodsAdapter
import com.kien.petclub.presentation.product.goods.GoodsViewModel
import com.kien.petclub.presentation.utils.PopupMenuHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BarcodeFragment<FragmentSearchProductBinding>(), ProductListener,
    SortProductListener {

    companion object {
        private const val SCANNER_CODE = 199
    }

    private var dataSource = ArrayList<Product>()

    private var searchResult = ArrayList<Product>()

    private var isSearching = true

    private lateinit var adapter: GoodsAdapter

    private val goodsViewModel: GoodsViewModel by activityViewModels()

    private val viewModel: SearchProductViewModel by viewModels()

    override fun getViewBinding(): FragmentSearchProductBinding =
        FragmentSearchProductBinding.inflate(layoutInflater)

    override fun setUpViews() {
        super.setUpViews()
        binding.ivBack.setOnClickListener { backToPreviousScreen() }

        binding.etSearch.setOnDrawableRightClick {
            requestCameraPermissionAndStartScanner(SCANNER_CODE)
        }

        val filterPopup = PopupMenuHelper(
            requireActivity(),
            R.menu.filter_price_menu, { item ->
                when (item.itemId) {
                    R.id.action_selling_price -> {
                        binding.filterPrice.text = getString(R.string.selling_price)
                        viewModel.setFilterProduct(getString(R.string.selling_price))
                        adapter.setFilterPrice(GoodsAdapter.SELLING_PRICE)
                        true
                    }

                    R.id.action_buying_price -> {
                        binding.filterPrice.text = getString(R.string.buying_price)
                        viewModel.setFilterProduct(getString(R.string.buying_price))
                        adapter.setFilterPrice(GoodsAdapter.BUYING_PRICE)
                        true
                    }

                    else -> false
                }
            }
        )

        binding.filterPrice.setOnClickListener {
            filterPopup.show(it)
        }

        binding.filterPrice.updateText(viewModel.getFilterProduct())

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isSearching = true
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) return
                searchResult = dataSource
                    .filter {
                        val lowerCase = s.toString().lowercase()
                        it.getName().lowercase().contains(lowerCase)
                                || it.getId().lowercase().contains(lowerCase)
                                || it.getCode().lowercase().contains(lowerCase)
                    }.toCollection(ArrayList())
                if (::adapter.isInitialized) {
                    adapter.setData(searchResult, viewModel.getSortProduct())
                }
                binding.tvResult.text = s.toString()
            }
        })

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }

        binding.ivSort.setOnClickListener {
            (requireActivity() as HomeActivity).showPopUpSort()
        }

        binding.ivSearch.setOnClickListener {
            showSearchBar()
        }

        showSearchBar()
        adapter = GoodsAdapter(this)
        binding.rvProduct.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProduct.adapter = adapter
    }

    private fun showSearchBar() {
        binding.line1.visibility = View.GONE
        binding.filterPrice.visibility = View.GONE
        binding.infoGoods.visibility = View.GONE
        binding.ivSearch.visibility = View.GONE
        binding.ivFilter.visibility = View.GONE
        binding.ivSort.visibility = View.GONE
        binding.etSearch.visibility = View.VISIBLE
        binding.tvResult.visibility = View.GONE
        isSearching = true
    }

    private fun hideSearchBar() {
        binding.etSearch.visibility = View.GONE
        binding.filterPrice.visibility = View.VISIBLE
        binding.infoGoods.visibility = View.VISIBLE
        binding.line1.visibility = View.VISIBLE
        binding.ivSearch.visibility = View.VISIBLE
        binding.ivFilter.visibility = View.VISIBLE
        binding.ivSort.visibility = View.VISIBLE
        binding.tvResult.visibility = View.VISIBLE
    }

    private fun performSearch() {
        hideSearchBar()
        isSearching = false
        setInfoTotalProduct(searchResult)
        adapter.sortData(viewModel.getSortProduct())
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

    override fun setupObservers() {
        super.setupObservers()
        lifecycleScope.launch {
            goodsViewModel.productResponse.flowWithLifecycle(lifecycle).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        dataSource.clear()
                        dataSource.addAll(resource.value)
                    }

                    else -> {}
                }
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.etSearch.visibility == View.VISIBLE && !isSearching) {
                    hideSearchBar()
                } else {
                    this.isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    this.isEnabled = true
                }
            }
        })
    }

    override fun onBarcodeScannerResult(data: String?, requestCode: Int) {
        super.onBarcodeScannerResult(data, requestCode)
        binding.etSearch.updateText(data)
    }

    override fun onSortClick(item: ChooserItem, position: Int) {
        adapter.sortData(position)
        viewModel.setSortProduct(position)
        (requireActivity() as HomeActivity).dismissPopUpSort()
    }

    override fun onItemClick(product: Product) {
        super.onItemClick(product)
        setFragmentResult(Constants.KEY_PRODUCT, Bundle().apply {
            putParcelable(Constants.DATA, product)
        })
        navigateSafe(SearchFragmentDirections.actionOpenDetailFragment())
    }

}