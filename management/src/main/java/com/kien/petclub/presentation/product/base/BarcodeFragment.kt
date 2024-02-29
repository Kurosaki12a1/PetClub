package com.kien.petclub.presentation.product.base

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.viewbinding.ViewBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.kien.petclub.extensions.checkAndRequestPermission
import com.kien.petclub.extensions.getResultLauncher
import com.kien.petclub.extensions.requestPermissionLauncher
import com.kien.petclub.presentation.base.BaseFragment

abstract class BarcodeFragment<VB : ViewBinding> : BaseFragment<VB>() {

    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
    }

    private var requestCode: Int = 0

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

    open fun onBarcodeScannerResult(data: String?, requestCode: Int) {}

}