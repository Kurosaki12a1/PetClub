package com.kien.petclub.presentation.product.edit_product

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.constants.Constants
import com.kien.petclub.databinding.FragmentAddProductBinding
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.model.entity.getPhoto
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.extensions.navigateSafe
import com.kien.petclub.extensions.updateText
import com.kien.petclub.presentation.product.base.BaseProductImageFragment
import com.kien.petclub.presentation.product.common.ShareMultiDataViewModel
import com.kien.petclub.presentation.product.utils.hideBottomNavigationAndFabButton
import com.kien.petclub.presentation.product.utils.hideLoadingAnimation
import com.kien.petclub.presentation.product.utils.showLoadingAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProductFragment : BaseProductImageFragment<FragmentAddProductBinding>() {

    private var typeInfo = Constants.EMPTY_STRING

    private lateinit var product: Product

    private var currentListImage = ArrayList<Uri>()

    private lateinit var typeProduct: String

    private val sharedViewModel: ShareMultiDataViewModel by activityViewModels()

    private val viewModel: EditProductViewModel by viewModels()

    private var job: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationAndFabButton()
    }

    override fun getViewTypes(viewType: String) {
        typeProduct = viewType
        when (viewType) {
            Constants.VALUE_GOODS -> {
                binding.title.setText(R.string.edit_goods)
                binding.areaInventory.visibility = View.VISIBLE
                binding.areaWeight.visibility = View.VISIBLE
                binding.areaLocation.visibility = View.VISIBLE
                binding.areaInventoryDetails.visibility = View.VISIBLE
            }

            Constants.VALUE_SERVICE -> {
                binding.title.setText(R.string.edit_service)
                binding.areaInventory.visibility = View.GONE
                binding.areaWeight.visibility = View.GONE
                binding.areaLocation.visibility = View.GONE
                binding.areaInventoryDetails.visibility = View.GONE
            }
        }
    }

    // Use same view AddProductActivity
    override fun getViewBinding(): FragmentAddProductBinding =
        FragmentAddProductBinding.inflate(layoutInflater)

    override fun setUpViews() {
        super.setUpViews()
        binding.ivBack.setOnClickListener { backToPreviousScreen() }

        binding.ivBarCode.setOnClickListener {
            requestCameraPermissionAndStartScanner(REQUEST_CODE_BARCODE_SERVICE)
        }

        binding.ivIdCode.setOnClickListener {
            requestCameraPermissionAndStartScanner(REQUEST_CODE_ID_SERVICE)
        }

        binding.save.setOnClickListener { submit() }


        binding.brandEdit.setOnClickListener {
            typeInfo = Constants.VALUE_BRAND
            navigateSafe(EditProductFragmentDirections.actionOpenAddInfoFragment(Constants.VALUE_BRAND))
        }

        binding.typeEdit.setOnClickListener {
            typeInfo = Constants.VALUE_TYPE
            navigateSafe(EditProductFragmentDirections.actionOpenAddInfoFragment(Constants.VALUE_TYPE))
        }

        binding.locationEdit.setOnClickListener {
            typeInfo = Constants.VALUE_LOCATION
            navigateSafe(EditProductFragmentDirections.actionOpenAddInfoFragment(Constants.VALUE_LOCATION))
        }

        binding.rvListPhoto.adapter = photoAdapter
        binding.rvListPhoto.setHasFixedSize(true)
        binding.rvListPhoto.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
    }

    override fun setupObservers() {
        super.setupObservers()
        sharedViewModel.setInfoProduct(null)

        job = lifecycleScope.launch {
            sharedViewModel.productResponse.collect {
                it?.let { updateUI(it) }
            }
        }

        lifecycleScope.launch {
            sharedViewModel.infoProductResponse.collect {
                if (it.isNullOrEmpty()) return@collect
                when (typeInfo) {
                    Constants.VALUE_BRAND -> binding.brandEdit.setText(it)
                    Constants.VALUE_TYPE -> binding.typeEdit.setText(it)
                    Constants.VALUE_LOCATION -> binding.locationEdit.setText(it)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.downloadResponse.collect {
                when (it) {
                    is Resource.Success -> {
                        hideLoadingAnimation()
                        currentListImage = ArrayList(it.value)
                        listImages.addAll(currentListImage)
                        photoAdapter.setData(listImages)
                    }

                    is Resource.Failure -> {
                        hideLoadingAnimation()
                    }

                    is Resource.Loading -> {
                        showLoadingAnimation()
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.updateResponse.collect {
                when (it) {
                    is Resource.Success -> {
                        job?.cancel()
                        sharedViewModel.setProduct(it.value)
                        hideLoadingAnimation()
                        backToPreviousScreen()
                    }

                    is Resource.Failure -> {
                        hideLoadingAnimation()
                    }

                    is Resource.Loading -> {
                        showLoadingAnimation()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun updateUI(product: Product) {
        this.product = product
        when (product) {
            is Product.Goods -> {
                binding.idEdit.updateText(product.id)
                binding.codeEdit.updateText(product.code)
                binding.nameEdit.updateText(product.name)
                binding.sellingPriceEdit.updateText(product.sellingPrice)
                binding.buyingPriceEdit.updateText(product.buyingPrice)
                binding.description.updateText(product.description)
                binding.note.updateText(product.note)
                binding.typeEdit.updateText(product.type)
                binding.brandEdit.updateText(product.brands)
                binding.inventoryEdit.updateText(product.stock)
                binding.weightEdit.updateText(product.weight)
                binding.locationEdit.updateText(product.location)

                if (product.photo != null) viewModel.downloadImage(product.photo)
            }

            is Product.Service -> {
                binding.idEdit.updateText(product.id)
                binding.codeEdit.updateText(product.code)
                binding.nameEdit.updateText(product.name)
                binding.sellingPriceEdit.updateText(product.sellingPrice)
                binding.buyingPriceEdit.updateText(product.buyingPrice)
                binding.description.updateText(product.description)
                binding.note.updateText(product.note)
                binding.typeEdit.updateText(product.type)
                binding.brandEdit.updateText(product.brands)

                if (product.photo != null) viewModel.downloadImage(product.photo)
            }
        }
    }


    override fun onBarcodeScannerResult(data: String?, requestCode: Int) {
        super.onBarcodeScannerResult(data, requestCode)
        when (requestCode) {
            REQUEST_CODE_ID_SERVICE -> binding.idEdit.setText(data ?: "")
            REQUEST_CODE_BARCODE_SERVICE -> binding.codeEdit.setText(data ?: "")
        }
    }

    private fun submit() {
        val id = binding.idEdit.text.toString()
        val code = binding.codeEdit.text.toString()
        val name = binding.nameEdit.text.toString()
        val sellingPrice = binding.sellingPriceEdit.text.toString()
        val buyingPrice = binding.buyingPriceEdit.text.toString()
        val description = binding.description.text.toString()
        val note = binding.note.text.toString()
        val type = binding.typeEdit.text.toString()
        val brand = binding.brandEdit.text.toString()
        val stock = binding.inventoryEdit.text.toString()
        val weight = binding.weightEdit.text.toString()
        val location = binding.locationEdit.text.toString()
        val minimumStock = binding.minimumInventoryEdit.text.toString()
        val maximumStock = binding.maximumInventoryEdit.text.toString()

        if (id.isEmpty()
            || name.isEmpty()
            || sellingPrice.isEmpty()
            || buyingPrice.isEmpty()
            || type.isEmpty()
            || brand.isEmpty()
        ) {
            //showToast("Please fill all fields")
            return
        }

        viewModel.updateProduct(
            typeProduct = typeProduct,
            id = id,
            code = code,
            name = name,
            type = type,
            brand = brand,
            sellingPrice = sellingPrice,
            buyingPrice = buyingPrice,
            stock = stock,
            weight = weight,
            location = location,
            description = description,
            note = note,
            newestPhoto = listImages.subtract(currentListImage.toSet()).toList(),
            photo = product.getPhoto(),
            minimumStock = minimumStock,
            maximumStock = maximumStock
        )
    }
}