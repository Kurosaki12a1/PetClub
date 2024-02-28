package com.kien.petclub.presentation.product.base

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.viewbinding.ViewBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.kien.imagepicker.presenter.ImagePickerActivity
import com.kien.imagepicker.utils.readUriListFromFile
import com.kien.petclub.constants.Constants
import com.kien.petclub.extensions.checkAndRequestPermission
import com.kien.petclub.extensions.getResultLauncher
import com.kien.petclub.extensions.requestPermissionLauncher
import com.kien.petclub.presentation.base.BaseFragment
import com.kien.petclub.presentation.product.ImagePickerListener
import com.kien.petclub.presentation.product.PickImageAdapter
import com.kien.petclub.presentation.product.utils.hideBottomNavigationAndFabButton

abstract class BaseProductImageFragment<VB : ViewBinding> : BaseFragment<VB>() {
    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        const val REQUEST_CODE_ID_SERVICE = 110
        const val REQUEST_CODE_BARCODE_SERVICE = 111
    }

    private var imageUri: Uri? = null

    private var requestCode: Int = 0

    lateinit var photoAdapter: PickImageAdapter

    var listImages = ArrayList<Uri>()

    abstract fun getViewTypes(viewType: String)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationAndFabButton()
        getViewTypes(
            arguments?.getString(Constants.KEY_TYPE, Constants.VALUE_GOODS) ?: Constants.VALUE_GOODS
        )
    }

    override fun setUpViews() {
        super.setUpViews()
        val widthItem = (Resources.getSystem().displayMetrics.widthPixels / 3.5f).toInt()
        photoAdapter = PickImageAdapter(object : ImagePickerListener {
            override fun onTakePhotoClick() {
                onImageClick()
            }

            override fun onDeletePhoto(uri: Uri, position: Int) {
                onDeletePhotoClick(uri, position)
            }
        }, widthItem)
        photoAdapter.setData(listImages)
    }

    open fun onImageClick() {
        getUriContent.launch(Intent(requireActivity(), ImagePickerActivity::class.java))
    }

    open fun onDeletePhotoClick(uri: Uri, position: Int) {}

    open fun onImagePickerResult(data: ArrayList<Uri>) {}

    open fun onBarcodeScannerResult(data: String?, requestCode: Int) {}

    fun requestCameraPermissionAndStartScanner(code: Int) {
        requestCode = code
        permissionCameraLauncher.launch(CAMERA_PERMISSION)
    }

    private fun startBarcodeScanner() {
        val integrator = IntentIntegrator(requireActivity())
        integrator.setOrientationLocked(false)
        val intent = integrator.createScanIntent()
        barcodeLauncher.launch(intent)
    }

    private val permissionCameraLauncher = requestPermissionLauncher { isGranted ->
        if (isGranted) {
            startBarcodeScanner()
        } else {
            checkAndRequestPermission(CAMERA_PERMISSION, 100)
        }
    }

    private val barcodeLauncher: ActivityResultLauncher<Intent> =
        getResultLauncher { resultCode, resultData ->
            if (resultCode == Activity.RESULT_OK) {
                val scanResult = IntentIntegrator.parseActivityResult(resultCode, resultData)
                onBarcodeScannerResult(scanResult.contents, requestCode)
            }
        }

    private val getUriContent = getResultLauncher { resultCode, _ ->
        if (resultCode == Activity.RESULT_OK) {
            onImagePickerResult(readUriListFromFile(requireActivity()))
        }
    }

}