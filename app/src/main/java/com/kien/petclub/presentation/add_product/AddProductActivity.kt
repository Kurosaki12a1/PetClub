package com.kien.petclub.presentation.add_product

import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.constants.Constants.VALUE_GOODS
import com.kien.petclub.constants.Constants.VALUE_SERVICE
import com.kien.petclub.databinding.ActivityAddProductBinding
import com.kien.petclub.extensions.showToast
import com.kien.petclub.presentation.base.AddActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductActivity : AddActivity<ActivityAddProductBinding>() {

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

        binding.rvListPhoto.adapter = photoAdapter
        binding.rvListPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    override fun getViewTypes(viewType: String) {
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
        val type = binding.typeEdit.selectedItem.toString()
        val brand = binding.brandEdit.selectedItem.toString()

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

        viewModel.submit(
            id = id,
            code = code,
            name = name,
            sellingPrice = sellingPrice,
            buyingPrice = buyingPrice,
            description = description,
            note = note,
            type = type,
            brand = brand,
            photo = null
        )
    }


}