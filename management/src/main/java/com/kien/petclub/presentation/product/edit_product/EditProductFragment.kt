package com.kien.petclub.presentation.product.edit_product

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.constants.Constants
import com.kien.petclub.databinding.FragmentAddProductBinding
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.model.entity.deletePhoto
import com.kien.petclub.domain.model.entity.getPhoto
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.extensions.navigateSafe
import com.kien.petclub.extensions.updateText
import com.kien.petclub.presentation.product.base.BaseProductImageFragment
import com.kien.petclub.presentation.product.utils.hideBottomNavigationAndFabButton
import com.kien.petclub.presentation.product.utils.hideLoadingAnimation
import com.kien.petclub.presentation.product.utils.showLoadingAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProductFragment : BaseProductImageFragment<FragmentAddProductBinding>() {

    private var typeInfo = Constants.EMPTY_STRING

    // List images that added before
    private var currentListImage = ArrayList<Uri>()

    private lateinit var typeProduct: String

    private lateinit var product: Product

    private val viewModel: EditProductViewModel by viewModels()

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

        parentFragmentManager.setFragmentResultListener(
            Constants.KEY_INFO_PRODUCT,
            viewLifecycleOwner
        ) { _, bundle ->
            val result = bundle.getString(Constants.DATA)
            result?.let {
                when (typeInfo) {
                    Constants.VALUE_BRAND -> binding.brandEdit.setText(it)
                    Constants.VALUE_TYPE -> binding.typeEdit.setText(it)
                    Constants.VALUE_LOCATION -> binding.locationEdit.setText(it)
                }
            }
        }

        arguments?.let {bundle ->
            val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(Constants.KEY_PRODUCT, Product::class.java)
            } else {
                bundle.getParcelable(Constants.KEY_PRODUCT)
            }
            result?.let { updateUI(it) }
        }
    }

    override fun setupObservers() {
        super.setupObservers()

        lifecycleScope.launch {
            // Get Images added before form Firebase
            viewModel.downloadResponse.flowWithLifecycle(lifecycle).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        hideLoadingAnimation()
                        if (it.value.isEmpty()) return@collectLatest
                        val tempList = it.value.toCollection(ArrayList())
                        if (currentListImage != tempList) {
                            currentListImage = tempList
                            listImages.addAll(currentListImage)
                            photoAdapter.setData(listImages)
                        }
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
            viewModel.updateResponse.flowWithLifecycle(lifecycle).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        setFragmentResult(Constants.KEY_PRODUCT, Bundle().apply {
                            putParcelable(Constants.DATA, it.value)
                        })
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
                if (product.photo != null) viewModel.downloadImage(product.photo!!)
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

                if (product.photo != null) viewModel.downloadImage(product.photo!!)
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

    override fun onImagePickerResult(data: ArrayList<Uri>) {
        super.onImagePickerResult(data)
        listImages.addAll(data)
        photoAdapter.setData(listImages)
    }

    override fun onDeletePhotoClick(uri: Uri, position: Int) {
        super.onDeletePhotoClick(uri, position)
        val tempList = listImages.map { it } as ArrayList<Uri>
        listImages.clear()
        // Case delete image added before
        if (position < currentListImage.size) {
            // Remove image from list added before
            currentListImage.removeAt(position)
            // Delete image from Firebase
            product.deletePhoto(position)
        }
        // Remove image from list
        tempList.removeAt(position)
        listImages.addAll(tempList)
        photoAdapter.setData(listImages)
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