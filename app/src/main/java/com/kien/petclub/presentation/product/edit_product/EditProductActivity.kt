package com.kien.petclub.presentation.product.edit_product

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.PetClubApplication
import com.kien.petclub.R
import com.kien.petclub.constants.Constants
import com.kien.petclub.databinding.ActivityAddProductBinding
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.initTransitionClose
import com.kien.petclub.extensions.showToast
import com.kien.petclub.extensions.updateText
import com.kien.petclub.presentation.base.ProductActivity
import com.kien.petclub.presentation.base.SharedViewModel
import com.kien.petclub.presentation.product.add_info_product.AddInfoProductActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditProductActivity : ProductActivity<ActivityAddProductBinding>() {

    private var currentListImage = ArrayList<Uri>()

    private lateinit var typeProduct: String

    private val viewModel: EditProductViewModel by viewModels()

    private val sharedProductVM: SharedViewModel<Product> by lazy {
        (application as PetClubApplication).sharedProductVM
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
    override fun getViewBinding(): ActivityAddProductBinding =
        ActivityAddProductBinding.inflate(layoutInflater)

    override fun setUpViews() {
        super.setUpViews()
        binding.ivBack.setOnClickListener { finish() }

        binding.ivBarCode.setOnClickListener {
            requestCameraPermissionAndStartScanner(REQUEST_CODE_BARCODE_SERVICE)
        }

        binding.ivIdCode.setOnClickListener {
            requestCameraPermissionAndStartScanner(REQUEST_CODE_ID_SERVICE)
        }

        binding.save.setOnClickListener { submit() }

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val type =
                        result.data?.getStringExtra(Constants.KEY_TYPE) ?: Constants.EMPTY_STRING
                    val data = result.data?.getStringExtra(Constants.DATA) ?: Constants.EMPTY_STRING
                    when (type) {
                        Constants.VALUE_BRAND -> binding.brandEdit.setText(data)
                        Constants.VALUE_TYPE -> binding.typeEdit.setText(data)
                        Constants.VALUE_LOCATION -> binding.locationEdit.setText(data)
                    }
                }
            }

        binding.brandEdit.setOnClickListener {
            val intent = Intent(this, AddInfoProductActivity::class.java)
            intent.putExtra(Constants.KEY_TYPE, Constants.VALUE_BRAND)
            resultLauncher.launch(intent)
        }

        binding.typeEdit.setOnClickListener {
            val intent = Intent(this, AddInfoProductActivity::class.java)
            intent.putExtra(Constants.KEY_TYPE, Constants.VALUE_TYPE)
            resultLauncher.launch(intent)
        }

        binding.locationEdit.setOnClickListener {
            val intent = Intent(this, AddInfoProductActivity::class.java)
            intent.putExtra(Constants.KEY_TYPE, Constants.VALUE_LOCATION)
            resultLauncher.launch(intent)
        }

        binding.rvListPhoto.adapter = photoAdapter
        binding.rvListPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun setUpObserver() {
        super.setUpObserver()

        lifecycleScope.launch {
            sharedProductVM.data.collect {
                if (it != null) {
                    updateUI(it)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.downloadResponse.collect {
                when (it) {
                    is Resource.Success -> {
                        stopLoadingAnimation()
                        currentListImage = ArrayList(it.value)
                        listImages.addAll(currentListImage)
                        photoAdapter.setData(listImages)
                    }

                    is Resource.Failure -> {
                        stopLoadingAnimation()
                    }

                    is Resource.Loading -> {
                        showLoadingAnimation()
                    }

                    else -> {
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.updateResponse.collect {
                when (it) {
                    is Resource.Success -> {
                        sharedProductVM.setData(it.value)
                        stopLoadingAnimation()
                        initTransitionClose()
                        finish()
                    }

                    is Resource.Failure -> {
                        stopLoadingAnimation()
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

        if (id.isEmpty()
            || name.isEmpty()
            || sellingPrice.isEmpty()
            || buyingPrice.isEmpty()
            || type.isEmpty()
            || brand.isEmpty()
        ) {
            showToast("Please fill all fields")
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
            photo = listImages.subtract(currentListImage.toSet()).toList()
        )
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