package com.kien.petclub.presentation.product.add_product

import android.net.Uri
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.constants.Constants
import com.kien.petclub.databinding.FragmentAddProductBinding
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.extensions.navigateSafe
import com.kien.petclub.presentation.product.base.BaseProductImageFragment
import com.kien.petclub.presentation.product.ShareMultiDataViewModel
import com.kien.petclub.presentation.product.utils.hideLoadingAnimation
import com.kien.petclub.presentation.product.utils.showLoadingAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddProductFragment : BaseProductImageFragment<FragmentAddProductBinding>() {

    private var typeInfo = Constants.EMPTY_STRING

    private lateinit var typeProduct: String

    private val viewModel: AddProductViewModel by viewModels()

    private val sharedVM: ShareMultiDataViewModel by activityViewModels()

    override fun getViewBinding() = FragmentAddProductBinding.inflate(layoutInflater)

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
            navigateSafe(AddProductFragmentDirections.actionOpenAddInfoFragment(Constants.VALUE_BRAND))
        }

        binding.typeEdit.setOnClickListener {
            typeInfo = Constants.VALUE_TYPE
            navigateSafe(AddProductFragmentDirections.actionOpenAddInfoFragment(Constants.VALUE_TYPE))
        }

        binding.locationEdit.setOnClickListener {
            typeInfo = Constants.VALUE_LOCATION
            navigateSafe(AddProductFragmentDirections.actionOpenAddInfoFragment(Constants.VALUE_LOCATION))
        }

        binding.rvListPhoto.adapter = photoAdapter
        binding.rvListPhoto.setHasFixedSize(true)
        binding.rvListPhoto.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
    }

    override fun onImagePickerResult(data: ArrayList<Uri>) {
        super.onImagePickerResult(data)
        listImages.addAll(data)
        photoAdapter.setData(listImages)
    }

    override fun setupObservers() {
        super.setupObservers()
        sharedVM.setInfoProduct(null)

        viewModel.response.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is Resource.Success -> {
                    hideLoadingAnimation()
                    backToPreviousScreen()
                }

                is Resource.Failure -> hideLoadingAnimation()

                is Resource.Loading -> showLoadingAnimation()

                else -> {}
            }
        }.launchIn(lifecycleScope)

        sharedVM.infoProductResponse.flowWithLifecycle(lifecycle).onEach {
            if (it.isNullOrEmpty()) return@onEach
            when (typeInfo) {
                Constants.VALUE_BRAND -> binding.brandEdit.setText(it)
                Constants.VALUE_TYPE -> binding.typeEdit.setText(it)
                Constants.VALUE_LOCATION -> binding.locationEdit.setText(it)
            }
        }.launchIn(lifecycleScope)
    }

    override fun getViewTypes(viewType: String) {
        typeProduct = viewType
        when (viewType) {
            Constants.VALUE_GOODS -> {
                binding.title.setText(R.string.add_goods)
                binding.areaInventory.visibility = View.VISIBLE
                binding.areaWeight.visibility = View.VISIBLE
                binding.areaLocation.visibility = View.VISIBLE
                binding.areaInventoryDetails.visibility = View.VISIBLE
            }

            Constants.VALUE_SERVICE -> {
                binding.title.setText(R.string.add_service)
                binding.areaInventory.visibility = View.GONE
                binding.areaWeight.visibility = View.GONE
                binding.areaLocation.visibility = View.GONE
                binding.areaInventoryDetails.visibility = View.GONE
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

        if (id.isEmpty()
            || name.isEmpty()
            || sellingPrice.isEmpty()
            || buyingPrice.isEmpty()
            || type.isEmpty()
            || brand.isEmpty()
        ) {
            //   showToast("Please fill all fields")
            return
        }

        if (typeProduct == Constants.VALUE_GOODS) {
            val stock = binding.inventoryEdit.text.toString()
            val weight = binding.weightEdit.text.toString()
            val location = binding.locationEdit.text.toString()
            val minimumStock = binding.minimumInventoryEdit.text.toString()
            val maximumStock = binding.maximumInventoryEdit.text.toString()

            if (stock.isEmpty() || weight.isEmpty() || location.isEmpty()) {
                //   showToast("Please fill all fields")
                return
            }

            viewModel.addGoods(
                id = id,
                code = code,
                name = name,
                sellingPrice = sellingPrice,
                buyingPrice = buyingPrice,
                description = description,
                note = note,
                type = type,
                brand = brand,
                stock = stock,
                weight = weight,
                location = location,
                photo = listImages,
                minimumStock = minimumStock,
                maximumStock = maximumStock
            )
        } else {
            viewModel.addService(
                id = id,
                code = code,
                name = name,
                sellingPrice = sellingPrice,
                buyingPrice = buyingPrice,
                description = description,
                note = note,
                type = type,
                brand = brand,
                photo = listImages
            )
        }
    }

}
