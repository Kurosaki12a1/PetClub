package com.kien.petclub.presentation.add_product

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.constants.Constants.DATA
import com.kien.petclub.constants.Constants.EMPTY_STRING
import com.kien.petclub.constants.Constants.KEY_TYPE
import com.kien.petclub.constants.Constants.VALUE_BRAND
import com.kien.petclub.constants.Constants.VALUE_GOODS
import com.kien.petclub.constants.Constants.VALUE_LOCATION
import com.kien.petclub.constants.Constants.VALUE_SERVICE
import com.kien.petclub.constants.Constants.VALUE_TYPE
import com.kien.petclub.databinding.ActivityAddProductBinding
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.initTransitionClose
import com.kien.petclub.extensions.showToast
import com.kien.petclub.presentation.add_info_product.AddInfoProductActivity
import com.kien.petclub.presentation.base.AddActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class AddProductActivity : AddActivity<ActivityAddProductBinding>() {

    private lateinit var typeProduct: String

    private val viewModel: AddProductViewModel by viewModels()

    override fun getViewBinding() = ActivityAddProductBinding.inflate(layoutInflater)

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
                    val type = result.data?.getStringExtra(KEY_TYPE) ?: EMPTY_STRING
                    val data = result.data?.getStringExtra(DATA) ?: EMPTY_STRING
                    when (type) {
                        VALUE_BRAND -> binding.brandEdit.setText(data)
                        VALUE_TYPE -> binding.typeEdit.setText(data)
                        VALUE_LOCATION -> binding.locationEdit.setText(data)
                    }
                }
            }

        binding.brandEdit.setOnClickListener {
            val intent = Intent(this, AddInfoProductActivity::class.java)
            intent.putExtra(KEY_TYPE, VALUE_BRAND)
            resultLauncher.launch(intent)
        }

        binding.typeEdit.setOnClickListener {
            val intent = Intent(this, AddInfoProductActivity::class.java)
            intent.putExtra(KEY_TYPE, VALUE_TYPE)
            resultLauncher.launch(intent)
        }

        binding.locationEdit.setOnClickListener {
            val intent = Intent(this, AddInfoProductActivity::class.java)
            intent.putExtra(KEY_TYPE, VALUE_LOCATION)
            resultLauncher.launch(intent)
        }

        binding.rvListPhoto.adapter = photoAdapter
        binding.rvListPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun setUpObserver() {
        super.setUpObserver()
        viewModel.response.onEach {
            when (it) {
                is Resource.Success -> {
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
        }.launchIn(lifecycleScope)
    }

    override fun getViewTypes(viewType: String) {
        typeProduct = viewType
        when (viewType) {
            VALUE_GOODS -> {
                binding.title.setText(R.string.add_goods)
                binding.areaInventory.visibility = View.VISIBLE
                binding.areaWeight.visibility = View.VISIBLE
                binding.areaLocation.visibility = View.VISIBLE
                binding.areaInventoryDetails.visibility = View.VISIBLE
            }

            VALUE_SERVICE -> {
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
            showToast("Please fill all fields")
            return
        }

        if (typeProduct == VALUE_GOODS) {
            val stock = binding.inventoryEdit.text.toString()
            val weight = binding.weightEdit.text.toString()
            val location = binding.locationEdit.text.toString()

            if (stock.isEmpty() || weight.isEmpty() || location.isEmpty()) {
                showToast("Please fill all fields")
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
                photo = listImages
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

    private fun showLoadingAnimation() {
        binding.loadingAnimationView.visibility = View.VISIBLE
        binding.loadingAnimationView.playAnimation()
    }

    private fun stopLoadingAnimation() {
        binding.loadingAnimationView.visibility = View.GONE
        binding.loadingAnimationView.cancelAnimation()
    }
}