package com.kien.petclub.presentation.product.base

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kien.imagepicker.R
import com.kien.imagepicker.adapter.ImagePickerAdapter
import com.kien.petclub.R.drawable
import com.kien.petclub.R.layout
import com.kien.petclub.databinding.AddPhotoItemBinding
import com.kien.petclub.presentation.product.common.ImagePickerListener

class PickImageAdapter(
    private var listener : ImagePickerListener,
    private var size : Int
) : RecyclerView.Adapter<PickImageAdapter.AddPhotoViewHolder>() {

    private var listUriImage = ArrayList<Uri>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddPhotoViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(layout.add_photo_item, parent, false)
        return AddPhotoViewHolder(AddPhotoItemBinding.bind(root))
    }

    override fun onBindViewHolder(holder: AddPhotoViewHolder, position: Int) {
        // When no item
        if (listUriImage.size == 0) {
            holder.ivRemove.visibility = View.GONE
            holder.itemView.setOnClickListener {
                listener.onTakePhotoClick()
            }
            return
        }

        if (position < listUriImage.size) {
            holder.ivRemove.visibility = View.VISIBLE

            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_add_camera)
                .transform(CenterCrop(), RoundedCorners(ImagePickerAdapter.RADIUS))
                .override(size)

            Glide.with(holder.itemView.context)
                .load(listUriImage[position])
                .apply(requestOptions)
                .into(holder.ivPhoto)
        } else {
            holder.ivRemove.visibility = View.GONE
            holder.ivPhoto.setImageResource(drawable.ic_add_camera)
            holder.itemView.setOnClickListener {
                listener.onTakePhotoClick()
            }
        }
    }

    override fun getItemCount(): Int = listUriImage.size + 1 // The Last item is add photo

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listUri: ArrayList<Uri>) {
        listUriImage.clear()
        listUriImage = ArrayList(listUri)
        notifyDataSetChanged()
    }

    inner class AddPhotoViewHolder(binding: AddPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivPhoto = binding.ivAddPhoto
        val ivRemove = binding.ivRemovePhoto
    }
}