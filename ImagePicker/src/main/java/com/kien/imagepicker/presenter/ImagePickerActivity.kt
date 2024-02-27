package com.kien.imagepicker.presenter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kien.imagepicker.R
import com.kien.imagepicker.data.entity.Album
import com.kien.imagepicker.databinding.ActivityImagePickerBinding
import com.kien.imagepicker.extensions.initTransitionClose
import com.kien.imagepicker.extensions.isInVisibleRect
import com.kien.imagepicker.extensions.slideDownAnimation
import com.kien.imagepicker.extensions.slideUpAnimation
import com.kien.imagepicker.presenter.adapter.AlbumPickerAdapter
import com.kien.imagepicker.presenter.adapter.ImagePickerAdapter
import com.kien.imagepicker.utils.createImageUri
import com.kien.imagepicker.utils.saveUriListsToFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * An activity for picking images, equipped with album and photo browsing capabilities.
 *
 * Utilizes [AndroidEntryPoint] for dependency injection with Hilt, facilitating ViewModel
 * and other Android framework dependencies' injections.
 * @author Thinh Huynh
 * @since 27/02/2024
 */
@AndroidEntryPoint
class ImagePickerActivity : AppCompatActivity(), ImagePickerListener {
    companion object {
        // Permissions required for reading and writing to external storage, and camera access
        private const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val CAMERA = Manifest.permission.CAMERA

        // Request codes for permission requests
        private const val REQUEST_CODE_READ_FILE = 0
        private const val REQUEST_CODE_TAKE_PICTURE = 1

        // Number of columns in the grid layout for displaying images
        private const val COLUMN = 3
    }

    // Binding for the activity's views
    private lateinit var binding: ActivityImagePickerBinding

    // Adapter for the RecyclerView displaying images
    private lateinit var adapter: ImagePickerAdapter

    // Adapter for the RecyclerView displaying albums
    private lateinit var albumAdapter: AlbumPickerAdapter

    // Currently selected album index
    private var selectedAlbum = 0

    // ViewModel for managing UI-related data
    private val viewModel: ImagePickerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Request permission to read external storage upon creation
        requestPermission(arrayOf(READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_FILE)

        // Initialize UI components and observe ViewModel data
        setUpViews()
        setUpObserver()

        // Handle back press with custom behavior
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Hide album RecyclerView with animation if visible, else perform default action
                if (binding.rvAlbum.visibility == View.VISIBLE) {
                    binding.rvAlbum.slideDownAnimation()
                    return
                }
                initTransitionClose()
            }
        })
    }

    /**
     * Sets up UI components, including RecyclerViews for displaying photos and albums.
     */
    private fun setUpViews() {
        val widthItem = Resources.getSystem().displayMetrics.widthPixels / COLUMN
        adapter = ImagePickerAdapter(this, widthItem)
        setUpRecyclerView()
        setUpAlbumRecyclerView()

        binding.ivBack.setOnClickListener {
            initTransitionClose()
        }

        binding.save.setOnClickListener {
            // Save selected images and return to previous activity
            saveUriListsToFile(this, adapter.imagesChosen.map { it.uri } as ArrayList<Uri>)
            setResult(Activity.RESULT_OK, Intent())
            initTransitionClose()
        }

        binding.tvAlbum.setOnClickListener {
            // Toggle visibility of album RecyclerView with animations
            if (binding.rvAlbum.visibility != View.VISIBLE) {
                binding.rvAlbum.slideUpAnimation()
            } else {
                binding.rvAlbum.slideDownAnimation()
            }
        }
    }

    /**
     * Initializes the RecyclerView for displaying photos with grid layout.
     */
    private fun setUpRecyclerView() {
        binding.rvPhoto.layoutManager =
            GridLayoutManager(this, COLUMN, GridLayoutManager.VERTICAL, false)
        binding.rvPhoto.adapter = adapter
        binding.rvPhoto.setHasFixedSize(true)
        binding.rvPhoto.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                val column = position % COLUMN
                val spacing = resources.getDimensionPixelSize(R.dimen.dimen5)
                outRect.left = spacing - column * spacing / COLUMN
                outRect.right = (column + 1) * spacing / COLUMN

                if (position < COLUMN) { // top edge
                    outRect.top = spacing
                }
                outRect.bottom = spacing // item bottom
            }
        })
    }

    private fun setUpAlbumRecyclerView() {
        albumAdapter = AlbumPickerAdapter(this)
        binding.rvAlbum.layoutManager = LinearLayoutManager(this)
        binding.rvAlbum.adapter = albumAdapter
    }

    /**
     * Observes data from the ViewModel to update UI components.
     */
    private fun setUpObserver() {
        lifecycleScope.launch {
            viewModel.albums.flowWithLifecycle(lifecycle).collect { list ->
                if (list.isNotEmpty()) {
                    if (binding.rvPhoto.visibility == View.GONE) {
                        binding.rvPhoto.visibility = View.VISIBLE
                    }
                    binding.tvAlbum.text = list[selectedAlbum].name
                    albumAdapter.setData(ArrayList(list))
                    viewModel.getPhotos(list[selectedAlbum])
                }
            }
        }

        lifecycleScope.launch {
            viewModel.photos.flowWithLifecycle(lifecycle).collectLatest {
                adapter.submitData(lifecycle, it)
                // Data keep showing wrong so we must notify
                binding.rvPhoto.layoutManager?.smoothScrollToPosition(binding.rvPhoto, null, 0)
            }
        }
    }

    /**
     * Requests the necessary permissions for the activity.
     */
    private fun requestPermission(permissions: Array<out String>, code: Int) {
        requestPermissions(permissions, code)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_READ_FILE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.getAlbums()
                } else {
                    // Cannot get images
                }
            }

            REQUEST_CODE_TAKE_PICTURE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePictureLauncher.launch(createImageUri(contentResolver))
                } else {
                    // Cannot take images
                }
            }

        }
    }

    override fun onTakePhoto() {
        super.onTakePhoto()
        requestPermission(arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE), REQUEST_CODE_TAKE_PICTURE)
    }

    override fun onAlbumClick(album: Album, position: Int) {
        super.onAlbumClick(album, position)
        // Update UI based on the selected album
        binding.tvAlbum.text = album.name
        selectedAlbum = position
        // Fetch photos for the selected album
        viewModel.getPhotos(album)
        // Hide the album list
        binding.rvAlbum.slideDownAnimation()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // Dismiss album list if touch event occurs outside of its bounds
        if (ev?.action == MotionEvent.ACTION_DOWN && binding.rvAlbum.visibility == View.VISIBLE) {
            if (!binding.rvAlbum.isInVisibleRect(ev.rawX.toInt(), ev.rawY.toInt())) {
                binding.rvAlbum.slideDownAnimation()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                viewModel.getAlbums() // Update album
            }
        }
}