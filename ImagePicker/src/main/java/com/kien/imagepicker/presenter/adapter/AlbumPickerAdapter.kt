package com.kien.imagepicker.presenter.adapter

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
import com.kien.imagepicker.presenter.ImagePickerListener
import com.kien.imagepicker.R
import com.kien.imagepicker.databinding.AlbumPickerItemBinding
import com.kien.imagepicker.data.entity.Album

class AlbumPickerAdapter(private val listener: ImagePickerListener) :
    RecyclerView.Adapter<AlbumPickerAdapter.ViewHolder>() {

    private var albums = ArrayList<Album>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.album_picker_item, parent, false)
        return ViewHolder(AlbumPickerItemBinding.bind(root))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = albums[position]
        holder.tvAlbumName.text = data.name
        holder.tvImageCount.text =
            holder.itemView.context.getString(R.string.images_count, data.images.size.toString())
        if (data.images.isNotEmpty()) {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(CenterCrop(), RoundedCorners(ImagePickerAdapter.RADIUS))

            Glide.with(holder.itemView.context)
                .load(data.images[0].uri)
                .apply(requestOptions)
                .into(holder.ivAlbum)
        }

        if (data.isSelected) {
            holder.ivCheck.visibility = View.VISIBLE
        } else {
            holder.ivCheck.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            updateData(position)
            listener.onAlbumClick(data, position)
        }
    }

    override fun getItemCount(): Int = albums.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: ArrayList<Album>) {
        albums.clear()
        albums.addAll(newList)
        notifyDataSetChanged()
    }

    private fun updateData(position: Int) {
        val tempList = albums.map { it.copy() } as ArrayList
        tempList.forEach { it.isSelected = false }
        tempList[position].isSelected = true
        val diffResult = DiffUtil.calculateDiff(DiffCallback(albums, tempList))
        albums.clear()
        albums.addAll(tempList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(binding: AlbumPickerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivAlbum = binding.ivAlbum
        val tvAlbumName = binding.tvAlbumName
        val tvImageCount = binding.tvImageCount
        val ivCheck = binding.ivCheck
    }

    inner class DiffCallback(
        private val oldList: ArrayList<Album>,
        private val newList: ArrayList<Album>
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