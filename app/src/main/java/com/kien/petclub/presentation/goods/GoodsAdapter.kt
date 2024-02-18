package com.kien.petclub.presentation.goods

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.kien.petclub.R
import com.kien.petclub.databinding.ItemDetailProductBinding
import com.kien.petclub.domain.model.entity.Product

class GoodsAdapter(private var listener: OnClickListener) :
    RecyclerView.Adapter<GoodsAdapter.ViewHolder>() {

    companion object {
        const val BUYING_PRICE = 100
        const val SELLING_PRICE = 101
        private const val VALUE_GOODS = 0
        private const val VALUE_SERVICE = 1
    }

    private var filterPrice = SELLING_PRICE

    private var listProduct = ArrayList<Product>()

    override fun getItemViewType(position: Int): Int {
        return when (listProduct[position]) {
            is Product.Goods -> VALUE_GOODS
            is Product.Service -> VALUE_SERVICE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detail_product, parent, false)
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
                    val requestOptions = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.ic_app)
                    Glide.with(holder.itemView.context)
                        .load(data.photo[0])
                        .apply(requestOptions)
                        .into(holder.ivPhoto)
                }

                holder.itemView.setOnClickListener {
                    listener.onItemClick(data)
                }
            }

            is Product.Service -> {
                holder.tvName.text = data.name
                holder.tvPrice.text =
                    if (filterPrice == SELLING_PRICE) data.sellingPrice else data.buyingPrice
                holder.tvQuantity.visibility = View.GONE
                holder.tvId.text = data.id
                if (data.photo == null) {
                    holder.ivPhoto.setImageResource(R.mipmap.ic_app)
                } else {
                    val requestOptions = RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.mipmap.ic_app)
                    Glide.with(holder.itemView.context)
                        .load(data.photo[0])
                        .apply(requestOptions)
                        .into(holder.ivPhoto)
                }

                holder.itemView.setOnClickListener {
                    listener.onItemClick(data)
                }
            }
        }
    }

    fun setData(list: List<Product>) {
        listProduct.clear()
        listProduct.addAll(list)
        notifyDataSetChanged()
    }

    fun setFilterPrice(filter: Int) {
        filterPrice = filter
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemDetailProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvName = binding.tvName
        val tvPrice = binding.tvPrice
        val tvQuantity = binding.tvQuantity
        val tvId = binding.tvIdProduct
        val ivPhoto = binding.ivProduct
    }
}