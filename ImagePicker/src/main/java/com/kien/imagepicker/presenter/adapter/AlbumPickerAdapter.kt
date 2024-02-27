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
import com.kien.imagepicker.R
import com.kien.imagepicker.data.entity.Album
import com.kien.imagepicker.databinding.AlbumPickerItemBinding
import com.kien.imagepicker.presenter.ImagePickerListener

/**
 * Adapter for displaying albums in a RecyclerView.
 *
 * This adapter is responsible for binding album data to views represented by ViewHolder instances. It allows
 * users to pick an album from a displayed list. Each album displays its name, image count, and a thumbnail
 * of the first image in the album.
 *
 * @param listener An instance of [ImagePickerListener] to handle click events on album items.
 * @author Thinh Huynh
 * @since 27/02/2024
 */
class AlbumPickerAdapter(private val listener: ImagePickerListener) :
    RecyclerView.Adapter<AlbumPickerAdapter.ViewHolder>() {

    /**
     * A list of albums to display in the RecyclerView.
     */
    private var albums = ArrayList<Album>()

    /**
     * Creates new ViewHolder instances for album items.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.album_picker_item, parent, false)
        return ViewHolder(AlbumPickerItemBinding.bind(root))
    }

    /**
     * Binds the album data to the ViewHolder.
     *
     * This method updates the content of the ViewHolder to reflect the album at the given position.
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
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

    /**
     * Updates the adapter's data set with new albums and notifies any registered observers that the data set has changed.
     *
     * @param newList The new list of albums to be displayed.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: ArrayList<Album>) {
        albums.clear()
        albums.addAll(newList)
        notifyDataSetChanged()
    }

    /**
     * Updates the selection status of albums and notifies changes for efficient item updates.
     *
     * @param position The position of the album that was selected.
     */
    private fun updateData(position: Int) {
        val tempList = albums.map { it.copy() } as ArrayList
        tempList.forEach { it.isSelected = false }
        tempList[position].isSelected = true
        val diffResult = DiffUtil.calculateDiff(DiffCallback(albums, tempList))
        albums.clear()
        albums.addAll(tempList)
        diffResult.dispatchUpdatesTo(this)
    }


    /**
     * ViewHolder class for album items.
     *
     * Holds references to the UI components within the RecyclerView item for quick access.
     *
     * @param binding The binding for the album picker item.
     */
    inner class ViewHolder(binding: AlbumPickerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ivAlbum = binding.ivAlbum
        val tvAlbumName = binding.tvAlbumName
        val tvImageCount = binding.tvImageCount
        val ivCheck = binding.ivCheck
    }

    /**
     * Utility class to calculate the difference between two lists and output a list of update operations that converts the first list into the second one.
     *
     * It is used to minimize the number of updates needed to make the RecyclerView reflect changes in the albums list.
     *
     * @param oldList The old list of albums.
     * @param newList The new list of albums.
     */
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