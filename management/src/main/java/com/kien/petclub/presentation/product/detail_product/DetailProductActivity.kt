package com.kien.petclub.presentation.product.detail_product

import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.SharedViewModel
import com.kien.petclub.constants.Constants
import com.kien.petclub.databinding.ActivityDetailProductBinding
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.model.entity.getPhoto
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.initTransitionClose
import com.kien.petclub.extensions.initTransitionOpen
import com.kien.petclub.extensions.openActivity
import com.kien.petclub.extensions.showToast
import com.kien.petclub.presentation.base.BaseActivity
import com.kien.petclub.presentation.product.edit_product.EditProductActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailProductActivity : BaseActivity<ActivityDetailProductBinding>() {

    private lateinit var adapter: DetailProductAdapter

    private lateinit var product: Product

    private val sharedProductVM: SharedViewModel<Product> by viewModels()

    private val viewModel: DetailProductViewModel by viewModels()

    override fun getViewBinding(): ActivityDetailProductBinding =
        ActivityDetailProductBinding.inflate(layoutInflater)


    override fun setUpViews() {
        super.setUpViews()

        binding.ivBack.setOnClickListener {
            initTransitionClose()
            finish()
        }

        binding.ivOptions.setOnClickListener {
            showPopup(it)
        }

        binding.ivEdit.setOnClickListener {
            initTransitionOpen()
            openActivity(
                EditProductActivity::class.java,
                when (product) {
                    is Product.Goods -> Constants.KEY_TYPE to Constants.VALUE_GOODS
                    is Product.Service -> Constants.KEY_TYPE to Constants.VALUE_SERVICE
                }
            )
        }
    }

    override fun setUpObserver() {
        super.setUpObserver()
        lifecycleScope.launch {
            sharedProductVM.data.collect {
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
                        stopLoadingAnimation()
                        showToast(getString(R.string.delete_success))
                        initTransitionClose()
                        finish()
                    }

                    is Resource.Loading -> {
                        showLoadingAnimation()
                    }

                    is Resource.Failure -> {
                        stopLoadingAnimation()
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.getPhotoResponse.collect {
                when (it) {
                    is Resource.Success -> {
                        stopLoadingAnimation()
                        adapter.setData(it.value)
                    }

                    is Resource.Loading -> {
                        showLoadingAnimation()
                    }

                    is Resource.Failure -> {
                        stopLoadingAnimation()
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
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(this, view)
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

    private fun showLoadingAnimation() {
        binding.loadingAnimationView.visibility = View.VISIBLE
        binding.loadingAnimationView.playAnimation()
    }

    private fun stopLoadingAnimation() {
        binding.loadingAnimationView.visibility = View.GONE
        binding.loadingAnimationView.cancelAnimation()
    }

}