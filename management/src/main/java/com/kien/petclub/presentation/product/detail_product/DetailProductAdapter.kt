package com.kien.petclub.presentation.product.detail_product

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kien.imagepicker.adapter.ImagePickerAdapter
import com.kien.petclub.R
import com.kien.petclub.databinding.AddPhotoItemBinding

class DetailProductAdapter(private val size: Int) : RecyclerView.Adapter<DetailProductAdapter.ViewHolder>() {

    private var listPhoto = ArrayList<Uri>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(R.layout.add_photo_item, parent, false)
        return ViewHolder(AddPhotoItemBinding.bind(root))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listPhoto[position]

        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.mipmap.ic_app)
            .transform(CenterCrop(), RoundedCorners(ImagePickerAdapter.RADIUS))
            .override(size)

        Glide.with(holder.itemView.context)
            .load(data)
            .apply(requestOptions)
            .into(holder.ivPhoto)
    }

    override fun getItemCount(): Int = listPhoto.size
    @SuppressLint("NotifyDataSetChanged")
    fun setData(photos : List<Uri>) {
        listPhoto.clear()
        listPhoto = ArrayList(photos)
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: AddPhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivPhoto = binding.ivAddPhoto
    }

}