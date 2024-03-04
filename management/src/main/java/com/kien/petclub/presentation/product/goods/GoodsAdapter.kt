package com.kien.petclub.presentation.product.goods

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kien.petclub.R
import com.kien.petclub.databinding.ItemDetailProductBinding
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.model.entity.ProductSortType
import com.kien.petclub.domain.model.entity.getId
import com.kien.petclub.presentation.product.ProductListener

class GoodsAdapter(
    private var listener: ProductListener, filterPriceType: String
) : RecyclerView.Adapter<GoodsAdapter.ViewHolder>() {

    companion object {
        private const val SELLING_PRICE = "Giá bán"
    }

    private var filterPrice = filterPriceType

    private var sortedType = 0

    private var listProduct = ArrayList<Product>()

    // Avoid Recyclerview keep showing wrong image (working around)
    override fun getItemViewType(position: Int): Int = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.item_detail_product, parent, false)
        return ViewHolder(ItemDetailProductBinding.bind(root))
    }

    override fun getItemCount(): Int = listProduct.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val data = listProduct[position]) {
            is Product.Goods -> {
                holder.tvName.text = data.name
                holder.tvPrice.text =
                    if (filterPrice == SELLING_PRICE) data.sellingPrice else data.buyingPrice
                holder.tvQuantity.visibility = View.VISIBLE
                holder.tvQuantity.text = data.stock
                holder.tvId.text = data.id
                if (data.photo == null) {
                    holder.ivPhoto.setImageResource(R.mipmap.ic_app)
                } else {
                    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.ic_app).centerCrop()
                        .override(holder.itemView.context.resources.getDimensionPixelSize(R.dimen.dimen60))

                    Glide.with(holder.itemView.context).load(data.photo!![0]).apply(requestOptions)
                        .into(holder.ivPhoto)
                }

                holder.itemView.setOnClickListener {
                    listener.onItemClick(data)
                }
            }

            is Product.Service -> {
                holder.tvName.text = data.name
                holder.tvPrice.text =
                    if (filterPrice == holder.itemView.context.getString(R.string.selling_price)) data.sellingPrice else data.buyingPrice
                holder.tvQuantity.visibility = View.GONE
                holder.tvId.text = data.id
                if (data.photo == null) {
                    holder.ivPhoto.setImageResource(R.mipmap.ic_app)
                } else {
                    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.ic_app)
                    Glide.with(holder.itemView.context).load(data.photo!![0]).apply(requestOptions)
                        .into(holder.ivPhoto)
                }

                holder.itemView.setOnClickListener {
                    listener.onItemClick(data)
                }
            }
        }
    }

    fun sortData(sortType: Int) {
        this.sortedType = sortType
        val tempList =
            ProductSortType.getListSort(listProduct, sortedType, filterPrice == SELLING_PRICE)
        val diffResult = DiffUtil.calculateDiff(DiffCallback(listProduct, tempList))
        listProduct = tempList
        diffResult.dispatchUpdatesTo(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: ArrayList<Product>, sortType: Int? = null) {
        if (sortType != null) sortedType = sortType
        val tempList = ProductSortType.getListSort(list, sortedType, filterPrice == SELLING_PRICE)
        if (listProduct.isEmpty()) {
            listProduct = tempList
            notifyDataSetChanged()
            return
        }
        val diffResult = DiffUtil.calculateDiff(DiffCallback(listProduct, tempList))
        listProduct = tempList
        diffResult.dispatchUpdatesTo(this)
    }

    fun setFilterPrice(filter: String) {
        filterPrice = filter
        sortData(sortedType)
    }

    inner class ViewHolder(binding: ItemDetailProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.tvName
        val tvPrice = binding.tvPrice
        val tvQuantity = binding.tvQuantity
        val tvId = binding.tvIdProduct
        val ivPhoto = binding.ivProduct
    }

    inner class DiffCallback(
        private val oldList: ArrayList<Product>, private val newList: ArrayList<Product>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].getId() == newList[newItemPosition].getId()

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}