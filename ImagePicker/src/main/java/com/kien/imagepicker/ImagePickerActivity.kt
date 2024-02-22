package com.kien.imagepicker

import android.Manifest
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kien.imagepicker.adapter.AlbumPickerAdapter
import com.kien.imagepicker.adapter.ImagePickerAdapter
import com.kien.imagepicker.databinding.ActivityImagePickerBinding
import com.kien.imagepicker.entity.Album
import com.kien.imagepicker.entity.Photo
import com.kien.imagepicker.utils.createImageUri
import com.kien.imagepicker.utils.initTransitionClose
import com.kien.imagepicker.utils.isInVisibleRect
import com.kien.imagepicker.utils.saveUriListsToFile
import com.kien.imagepicker.utils.slideDownAnimation
import com.kien.imagepicker.utils.slideUpAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImagePickerActivity : AppCompatActivity(), ImagePickerListener {
    companion object {
        private const val READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE
        private const val CAMERA = Manifest.permission.CAMERA

        private const val REQUEST_CODE_READ_FILE = 0
        private const val REQUEST_CODE_TAKE_PICTURE = 1
        private const val COLUMN = 3
        private const val VISIBLE_THRESHOLD = 3
        private const val ITEM_PER_PAGE = 15
    }

    private lateinit var binding: ActivityImagePickerBinding

    private lateinit var adapter: ImagePickerAdapter

    private lateinit var albumAdapter: AlbumPickerAdapter

    private var selectedAlbum = 0

    private var isLoading = false

    private var images: ArrayList<Photo> = ArrayList()

    private var currentItemCount = 0

    private val viewModel: ImagePickerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePickerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermission(arrayOf(READ_EXTERNAL_STORAGE), REQUEST_CODE_READ_FILE)
        setUpViews()
        setUpObserver()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.rvAlbum.visibility == View.VISIBLE) {
                    binding.rvAlbum.slideDownAnimation()
                    return
                }
                initTransitionClose()
            }
        })
    }

    private fun setUpViews() {
        val widthItem = Resources.getSystem().displayMetrics.widthPixels / COLUMN
        adapter = ImagePickerAdapter(this, widthItem)
        setUpRecyclerView()
        setUpAlbumRecyclerView()

        binding.ivBack.setOnClickListener {
            initTransitionClose()
        }

        binding.save.setOnClickListener {
            saveUriListsToFile(this, adapter.imagesChosen.map { it.uri } as ArrayList<Uri>)
            setResult(Activity.RESULT_OK, Intent())
            initTransitionClose()
        }

        binding.tvAlbum.setOnClickListener {
            if (binding.rvAlbum.visibility != View.VISIBLE) {
                binding.rvAlbum.slideUpAnimation()
            } else {
                binding.rvAlbum.slideDownAnimation()
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvPhoto.layoutManager =
            GridLayoutManager(this, COLUMN, GridLayoutManager.VERTICAL, false)
        binding.rvPhoto.adapter = adapter
        binding.rvPhoto.setHasFixedSize(true)
        binding.rvPhoto.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                if (!isLoading && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD) {
                    isLoading = true
                    loadMoreItems()
                }
            }
        })
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

    private fun loadMoreItems() {
        isLoading = false
        if (images.size <= currentItemCount + ITEM_PER_PAGE) {
            adapter.addData(ArrayList(images.subList(currentItemCount, images.size)))
            currentItemCount = images.size
        } else {
            adapter.addData(
                ArrayList(
                    images.subList(
                        currentItemCount,
                        currentItemCount + ITEM_PER_PAGE - 1
                    )
                )
            )
            currentItemCount += ITEM_PER_PAGE
        }
    }

    private fun setUpObserver() {
        lifecycleScope.launch {
            viewModel.images.collect { list ->
                if (list.isNotEmpty()) {
                    if (binding.rvPhoto.visibility == View.GONE) {
                        binding.rvPhoto.visibility = View.VISIBLE
                    }
                    binding.tvAlbum.text = list[selectedAlbum].name
                    albumAdapter.setData(ArrayList(list))
                    setUpDataRecyclerView(list[selectedAlbum].images)
                }
            }
        }
    }

    private fun setUpDataRecyclerView(photos: ArrayList<Photo>) {
        images = ArrayList(photos.reversed())
        if (images.size > ITEM_PER_PAGE) {
            currentItemCount = ITEM_PER_PAGE
            adapter.setData(ArrayList(images.subList(0, currentItemCount - 1)))
        } else {
            currentItemCount = photos.size
            adapter.setData(images)
        }
    }

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
        binding.tvAlbum.text = album.name
        selectedAlbum = position
        setUpDataRecyclerView(album.images)
        binding.rvAlbum.slideDownAnimation()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
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