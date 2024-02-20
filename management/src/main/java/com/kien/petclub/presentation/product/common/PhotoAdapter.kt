package com.kien.petclub.presentation.product.common

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kien.petclub.R.drawable
import com.kien.petclub.R.layout
import com.kien.petclub.databinding.AddPhotoItemBinding

class PhotoAdapter(
    private var onItemClick: ((position: Int) -> Unit)
) : RecyclerView.Adapter<PhotoAdapter.AddPhotoViewHolder>() {

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
            holder.itemView.setOnClickListener { onItemClick.invoke(position) }
            return
        }

        // Set height = width
        holder.itemView.post {
            holder.itemView.layoutParams.width = holder.itemView.height
        }

        if (position < listUriImage.size) {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(drawable.ic_add_camera)

            Glide.with(holder.itemView.context)
                .load(listUriImage[position])
                .apply(requestOptions)
                .into(holder.ivPhoto)
        } else {
            holder.ivPhoto.setImageResource(drawable.ic_add_camera)
            holder.itemView.setOnClickListener {
                onItemClick.invoke(position)
            }
        }
    }

    override fun getItemCount(): Int = listUriImage.size + 1 // The Last item is add photo

    fun setData(listUri: ArrayList<Uri>) {
        listUriImage.clear()
        listUriImage = ArrayList(listUri)
        notifyDataSetChanged()
    }

    inner class AddPhotoViewHolder(binding: AddPhotoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivPhoto = binding.ivAddPhoto
    }
}