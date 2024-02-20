package com.kien.petclub.presentation.product.goods

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
import com.kien.petclub.domain.model.entity.getBuyingPrice
import com.kien.petclub.domain.model.entity.getId
import com.kien.petclub.domain.model.entity.getName
import com.kien.petclub.domain.model.entity.getSellingPrice
import com.kien.petclub.domain.model.entity.getStock
import com.kien.petclub.domain.model.entity.getUpdateDated
import com.kien.petclub.presentation.product.common.ProductListener

class GoodsAdapter(private var listener: ProductListener) :
    RecyclerView.Adapter<GoodsAdapter.ViewHolder>() {

    companion object {
        const val BUYING_PRICE = 100
        const val SELLING_PRICE = 101
        private const val VALUE_GOODS = 0
        private const val VALUE_SERVICE = 1

        private const val NEWEST = 0
        private const val OLDEST = 1
        private const val NAME_AZ = 2
        private const val NAME_ZA = 3
        private const val PRICE_LOW_TO_HIGH = 4
        private const val PRICE_HIGH_TO_LOW = 5
        private const val INVENTORY_LOW_TO_HIGH = 6
        private const val INVENTORY_HIGH_TO_LOW = 7
        private const val SELL_LOW_TO_HIGH = 8
        private const val SELL_HIGH_TO_LOW = 9
    }

    private var filterPrice = SELLING_PRICE

    private var sortedType = NEWEST

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

    private fun getSortedList(list: ArrayList<Product>, sortType: Int): ArrayList<Product> {
        val result: ArrayList<Product> = when (sortType) {
            NEWEST -> {
                ArrayList(list.sortedByDescending { it.getUpdateDated() })
            }

            OLDEST -> {
                ArrayList(list.sortedBy { it.getUpdateDated() })
            }

            NAME_AZ -> {
                ArrayList(list.sortedBy { it.getName() })
            }

            NAME_ZA -> {
                ArrayList(list.sortedByDescending { it.getName() })
            }

            PRICE_LOW_TO_HIGH -> {
                ArrayList(list.sortedBy { if (filterPrice == BUYING_PRICE) it.getBuyingPrice() else it.getSellingPrice() })
            }

            PRICE_HIGH_TO_LOW -> {
                ArrayList(list.sortedByDescending { if (filterPrice == BUYING_PRICE) it.getBuyingPrice() else it.getSellingPrice() })
            }

            INVENTORY_LOW_TO_HIGH -> {
                ArrayList(list.sortedBy { it.getStock() })
            }

            INVENTORY_HIGH_TO_LOW -> {
                ArrayList(list.sortedByDescending { it.getStock() })
            }

            /*    SELL_LOW_TO_HIGH -> {
                    list.sortedBy { it.getSellingPrice() }
                }

                SELL_HIGH_TO_LOW -> {
                    list.sortedByDescending { it.getSellingPrice() }
                }*/

            else -> ArrayList(list)
        }
        return result
    }

    fun sortData(sortType: Int) {
        this.sortedType = sortType
        val tempList = getSortedList(listProduct, sortedType)
        val diffResult = DiffUtil.calculateDiff(DiffCallback(listProduct, tempList))
        listProduct = tempList
        diffResult.dispatchUpdatesTo(this)
    }

    fun setData(list: ArrayList<Product>, sortType: Int? = null) {
        if (sortType != null) sortedType = sortType
        val tempList = getSortedList(list, sortedType)
        if (listProduct.isEmpty()) {
            listProduct = tempList
            notifyDataSetChanged()
            return
        }
        val diffResult = DiffUtil.calculateDiff(DiffCallback(listProduct, tempList))
        listProduct = tempList
        diffResult.dispatchUpdatesTo(this)
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

    inner class DiffCallback(
        private val oldList: ArrayList<Product>,
        private val newList: ArrayList<Product>
    ) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition].getId() == newList[newItemPosition].getId()

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}