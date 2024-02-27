package com.kien.imagepicker.presenter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kien.imagepicker.R
import com.kien.imagepicker.data.entity.Photo
import com.kien.imagepicker.databinding.PickPhotoItemBinding
import com.kien.imagepicker.presenter.ImagePickerListener

/**
 * An adapter for the RecyclerView used in an image picker, supporting paging and custom image handling.
 * @param listener An instance of ImagePickerListener for callback events.
 * @param size The size to which images should be resized.
 * @author Thinh Huynh
 * @since 27/02/2024
 */
class ImagePickerAdapter(private val listener: ImagePickerListener, private val size: Int) :
    PagingDataAdapter<Photo, ImagePickerAdapter.ViewHolder>(DIFF_CALLBACK) {

    // Tracks the images that have been selected.
    var imagesChosen = ArrayList<Photo>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.pick_photo_item, parent, false)
        return (ViewHolder(PickPhotoItemBinding.bind(root)))
    }

    // Avoid Recyclerview keep showing wrong image (working around)
    override fun getItemViewType(position: Int): Int {
        return if (position != FIRST_INDEX_ITEM) position
        else super.getItemViewType(position)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val requestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.ic_add_camera)
            .transform(CenterCrop(), RoundedCorners(RADIUS))
            .override(size)

        // Only first item is the camera item
        if (itemCount == 1) {
            Glide.with(holder.itemView.context)
                .load(R.drawable.ic_add_camera)
                .into(holder.ivPhoto)

            holder.ivChoose.visibility = View.GONE
            holder.itemView.setOnClickListener {
                listener.onTakePhoto()
            }
            return
        }

        if (position == FIRST_INDEX_ITEM) {
            Glide.with(holder.itemView.context)
                .load(R.drawable.ic_add_camera)
                .into(holder.ivPhoto)

            holder.ivChoose.visibility = View.GONE
            holder.itemView.setOnClickListener {
                listener.onTakePhoto()
            }
        } else {
            val data = getItem(position - 1)
            if (data?.uri == null) return
            holder.ivChoose.visibility = View.VISIBLE

            Glide.with(holder.itemView.context)
                .load(data.uri)
                .apply(requestOptions)
                .into(holder.ivPhoto)

            if (data.isSelected) {
                holder.ivChoose.setImageResource(R.drawable.ic_circle_checked)
            } else {
                holder.ivChoose.setImageResource(R.drawable.ic_circle_unchecked)
            }

            holder.itemView.setOnClickListener {
                data.isSelected = !data.isSelected
                if (data.isSelected) {
                    holder.ivChoose.setImageResource(R.drawable.ic_circle_checked)
                    imagesChosen.add(data)
                } else {
                    holder.ivChoose.setImageResource(R.drawable.ic_circle_unchecked)
                    imagesChosen.remove(data)
                }
            }
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + 1 // +1 for the camera item

    inner class ViewHolder(binding: PickPhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivPhoto = binding.ivPhoto
        val ivChoose = binding.ivChoose
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem.uri == newItem.uri

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem == newItem
        }

        private const val FIRST_INDEX_ITEM = 0
        const val RADIUS = 16
    }

}