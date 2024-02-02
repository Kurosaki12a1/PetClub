package com.kien.petclub.presentation.base

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewbinding.ViewBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.kien.petclub.constants.Constants.KEY_TYPE
import com.kien.petclub.constants.Constants.VALUE_GOODS
import com.kien.petclub.extensions.checkAndRequestPermission
import com.kien.petclub.extensions.getResultLauncher
import com.kien.petclub.extensions.requestPermissionLauncher
import com.kien.petclub.presentation.add_product.PhotoAdapter
import com.kien.petclub.utils.convertMillisToDate

abstract class AddActivity<VB : ViewBinding> : BaseActivity<VB>() {
    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        private const val WRITE_EXTERNAL_STORAGE_PERMISSION =
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        const val REQUEST_CODE_ID_SERVICE = 110
        const val REQUEST_CODE_BARCODE_SERVICE = 111
    }

    private var imageUri: Uri? = null

    private var requestCode: Int = 0

    lateinit var photoAdapter: PhotoAdapter

    var listImages = ArrayList<Uri>()

    abstract fun getViewTypes(viewType: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getViewTypes(intent?.getStringExtra(KEY_TYPE) ?: VALUE_GOODS)
    }

    override fun setUpViews() {
        super.setUpViews()
        photoAdapter = PhotoAdapter { onImageClick(it) }
        photoAdapter.setData(listImages)
    }

    open fun onImageClick(position: Int) {
        requestPermissionsAndStartTakePhoto()
    }

    private fun startBarcodeScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setOrientationLocked(false)
        val intent = integrator.createScanIntent()
        barcodeLauncher.launch(intent)
    }

    private fun takePicture() {
        imageUri = createImageUri()
        takePictureLauncher.launch(imageUri)
    }

    /* private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }*/ */

    private fun createImageUri(): Uri {
        val contentResolver = contentResolver
        val timeStamp = convertMillisToDate()
        val imageFileName = "PET_CLUB_$timeStamp.jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }


    // Override this method to handle the result of the barcode scanner
    open fun onBarcodeScannerResult(data: String?, requestCode: Int) {}

    /*   // For case get from storage
       private fun requestWriteExternalStoragePermissionAndPickImage() {
           permissionExternalStorageLauncher.launch(WRITE_EXTERNAL_STORAGE_PERMISSION)
       }*/

    private fun requestPermissionsAndStartTakePhoto() {
        checkAndRequestPermission(CAMERA_PERMISSION, 100)
        checkAndRequestPermission(WRITE_EXTERNAL_STORAGE_PERMISSION, 101)
        takePicture()
    }

    fun requestCameraPermissionAndStartScanner(code: Int) {
        requestCode = code
        permissionCameraLauncher.launch(CAMERA_PERMISSION)
    }

    private val permissionCameraLauncher = requestPermissionLauncher { isGranted ->
        if (isGranted) {
            startBarcodeScanner()
        } else {
            checkAndRequestPermission(CAMERA_PERMISSION, 100)
        }
    }

    /*    private val permissionExternalStorageLauncher = requestPermissionLauncher { isGranted ->
            if (isGranted) {
                pickImageFromGallery()
            } else {
                checkAndRequestPermission(WRITE_EXTERNAL_STORAGE_PERMISSION, 101)
            }
        }*/

    private val barcodeLauncher: ActivityResultLauncher<Intent> =
        getResultLauncher { resultCode, resultData ->
            if (resultCode == Activity.RESULT_OK) {
                val scanResult = IntentIntegrator.parseActivityResult(resultCode, resultData)
                onBarcodeScannerResult(scanResult.contents, requestCode)
            }
        }

    /* private val pickImageLauncher =
         registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
             uri?.let {
                 listImages.add(it)
                 photoAdapter.setData(listImages)
             }
         }*/

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri?.let {
                    listImages.add(it)
                    photoAdapter.setData(listImages)
                }
            }
        }
}