package com.kien.imagepicker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.kien.imagepicker.ImagePickerListener
import com.kien.imagepicker.R
import com.kien.imagepicker.databinding.PickPhotoItemBinding
import com.kien.imagepicker.entity.Photo

class ImagePickerAdapter(private val listener: ImagePickerListener, private val size: Int) :
    RecyclerView.Adapter<ImagePickerAdapter.ViewHolder>() {
    companion object {
        private const val FIRST_INDEX_ITEM = 0
        const val RADIUS = 16
    }

    private var images: ArrayList<Photo> = ArrayList()

    var imagesChosen: ArrayList<Photo> = ArrayList()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.pick_photo_item, parent, false)
        return (ViewHolder(PickPhotoItemBinding.bind(root)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (images.isEmpty()) {
            holder.ivPhoto.setImageResource(R.drawable.ic_add_camera)
            holder.ivChoose.visibility = View.GONE
            holder.itemView.setOnClickListener {
                listener.onTakePhoto()
            }
            return
        }

        if (position == FIRST_INDEX_ITEM) {
            holder.ivPhoto.setImageResource(R.drawable.ic_add_camera)
            holder.ivChoose.visibility = View.GONE
            holder.itemView.setOnClickListener {
                listener.onTakePhoto()
            }
        } else {
            val data = images[position - 1]
            holder.ivChoose.visibility = View.VISIBLE

            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_add_camera)
                .transform(CenterCrop(), RoundedCorners(RADIUS))
                .override(size)

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

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: ArrayList<Photo>) {
        images.clear()
        images.addAll(newList)
        notifyDataSetChanged()
    }

    fun addData(newList: ArrayList<Photo>) {
        val tempList = images.map { it.copy() } as ArrayList
        tempList.addAll(newList)
        val diffResult = DiffUtil.calculateDiff(DiffCallback(images, tempList))
        images.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount(): Int = images.size + 1 // +1 for the camera item

    inner class ViewHolder(binding: PickPhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivPhoto = binding.ivPhoto
        val ivChoose = binding.ivChoose
    }

    inner class DiffCallback(
        private val oldList: ArrayList<Photo>,
        private val newList: ArrayList<Photo>
    ) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}