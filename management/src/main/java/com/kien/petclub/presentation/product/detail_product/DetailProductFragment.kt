package com.kien.petclub.presentation.product.detail_product

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.constants.Constants
import com.kien.petclub.databinding.FragmentDetailProductBinding
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.model.entity.getPhoto
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.extensions.navigateSafe
import com.kien.petclub.presentation.product.base.BaseProductFragment
import com.kien.petclub.presentation.product.common.ShareMultiDataViewModel
import com.kien.petclub.presentation.product.utils.hideBottomNavigationAndFabButton
import com.kien.petclub.presentation.product.utils.hideLoadingAnimation
import com.kien.petclub.presentation.product.utils.showLoadingAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailProductFragment : BaseProductFragment<FragmentDetailProductBinding>() {
    private lateinit var adapter: DetailProductAdapter

    private lateinit var product: Product

    private val shareVM: ShareMultiDataViewModel by activityViewModels()

    private val viewModel: DetailProductViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationAndFabButton()
    }

    override fun getViewBinding(): FragmentDetailProductBinding =
        FragmentDetailProductBinding.inflate(layoutInflater)

    override fun setUpViews() {
        super.setUpViews()

        binding.ivBack.setOnClickListener {
            backToPreviousScreen()
        }

        binding.ivOptions.setOnClickListener {
            showPopup(it)
        }

        binding.ivEdit.setOnClickListener {
            navigateSafe(
                DetailProductFragmentDirections.actionOpenEditFragment(
                    when (product) {
                        is Product.Goods -> Constants.VALUE_GOODS
                        is Product.Service -> Constants.VALUE_SERVICE
                    }
                )
            )
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        lifecycleScope.launch {
            shareVM.productResponse.collect {
                if (it != null) {
                    product = it
                    updateUI(it)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.deleteResponse.collect {
                when (it) {
                    is Resource.Success -> {
                        hideLoadingAnimation()
                        //      showToast(getString(R.string.delete_success))
                        backToPreviousScreen()
                    }

                    is Resource.Loading -> {
                        showLoadingAnimation()
                    }

                    is Resource.Failure -> {
                        hideLoadingAnimation()
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getPhotoResponse.collect {
                when (it) {
                    is Resource.Success -> {
                        hideLoadingAnimation()
                        adapter.setData(it.value)
                    }

                    is Resource.Loading -> {
                        showLoadingAnimation()
                    }

                    is Resource.Failure -> {
                        hideLoadingAnimation()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun updateUI(product: Product) {
        when (product) {
            is Product.Goods -> {
                binding.tvStockDetail.visibility = View.VISIBLE
                binding.tvStockTitle.visibility = View.VISIBLE
                binding.tvWeightDetail.visibility = View.VISIBLE
                binding.tvWeightTitle.visibility = View.VISIBLE
                binding.ivStockDetail.visibility = View.VISIBLE
                binding.tvStockMeasureDetail.visibility = View.VISIBLE
                binding.tvStockMeasureTitle.visibility = View.VISIBLE

                binding.tvStockMeasureDetail.text = getString(
                    R.string.measure_stock_detail,
                    product.minimumStock,
                    product.maximumStock
                )

                binding.title.text = product.id
                binding.tvName.text = product.name
                binding.tvIdDetail.text = product.id
                binding.tvCodeDetail.text = product.code

                binding.tvGroupDetail.text = product.type
                binding.tvTypeDetail.text = getString(R.string.goods)
                binding.tvBrandDetail.text = product.brands
                binding.tvSellingDetail.text = product.sellingPrice
                binding.tvBuyingDetail.text = product.buyingPrice
                binding.tvStockDetail.text = product.stock
                binding.tvWeightDetail.text = product.weight
                binding.tvDescription.text = product.description
                binding.tvNote.text = product.note

            }

            is Product.Service -> {
                binding.tvStockDetail.visibility = View.GONE
                binding.tvStockTitle.visibility = View.GONE
                binding.tvWeightDetail.visibility = View.GONE
                binding.tvWeightTitle.visibility = View.GONE
                binding.ivStockDetail.visibility = View.GONE
                binding.tvStockMeasureDetail.visibility = View.GONE
                binding.tvStockMeasureTitle.visibility = View.GONE

                binding.title.text = product.id
                binding.tvName.text = product.name
                binding.tvIdDetail.text = product.id
                binding.tvCodeDetail.text = product.code

                binding.tvGroupDetail.text = product.type
                binding.tvTypeDetail.text = getString(R.string.services)
                binding.tvBrandDetail.text = product.brands
                binding.tvSellingDetail.text = product.sellingPrice
                binding.tvBuyingDetail.text = product.buyingPrice
                binding.tvDescription.text = product.description
                binding.tvNote.text = product.note
            }
        }

        if (!product.getPhoto().isNullOrEmpty()) {
            setUpRecyclerView()
            binding.dividerRecyclerView.visibility = View.VISIBLE
            viewModel.getListPhoto(product)
        } else {
            binding.dividerRecyclerView.visibility = View.GONE
            binding.rvPhoto.visibility = View.GONE
        }
    }

    private fun setUpRecyclerView() {
        binding.rvPhoto.visibility = View.VISIBLE
        adapter = DetailProductAdapter()
        binding.rvPhoto.adapter = adapter
        binding.rvPhoto.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(requireActivity(), view)
        popup.menuInflater.inflate(R.menu.detail_popup_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_print -> {
                    //TODO
                }

                R.id.action_stop_selling -> {
                    //TODO
                }

                R.id.action_delete -> {
                    viewModel.deleteProduct(product)
                }
            }
            true
        }
        popup.show()
    }
}