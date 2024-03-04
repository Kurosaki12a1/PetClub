package com.kien.petclub.presentation.product.filter_product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kien.petclub.R
import com.kien.petclub.data.data_source.local.entity.FilterProductEntity
import com.kien.petclub.databinding.ItemProductChooseFilterOptionsBinding
import com.kien.petclub.databinding.ItemProductFilterOptionsBinding
import com.kien.petclub.extensions.updateText
import com.kien.petclub.presentation.product.FilterProductListener

class FilterOptionsAdapter(
    private val filterProduct: FilterProductEntity,
    private val listener: FilterProductListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_SELECT_ONE_OPTIONS = 0
        private const val TYPE_SELECT_MULTI_OPTIONS = 1
    }

    override fun getItemViewType(position: Int): Int = if (filterProduct.isMultiOptions) {
        TYPE_SELECT_MULTI_OPTIONS
    } else {
        TYPE_SELECT_ONE_OPTIONS
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SELECT_ONE_OPTIONS -> {
                val root = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_product_filter_options, parent, false
                )
                SingleOptionsViewHolder(ItemProductFilterOptionsBinding.bind(root))
            }

            TYPE_SELECT_MULTI_OPTIONS -> {
                val root = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_product_choose_filter_options, parent, false
                )
                MultiOptionsViewHolder(ItemProductChooseFilterOptionsBinding.bind(root))
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = filterProduct.options.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = filterProduct.options[position]
        when (holder.itemViewType) {
            TYPE_SELECT_ONE_OPTIONS -> {
                (holder as SingleOptionsViewHolder).bind(data, position)
            }

            TYPE_SELECT_MULTI_OPTIONS -> {
                (holder as MultiOptionsViewHolder).bind(data)
            }
        }
    }

    inner class SingleOptionsViewHolder(binding: ItemProductFilterOptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val tvTitle = binding.tvOptions
        private val ivTick = binding.ivTick

        fun bind(data: String, position: Int) {
            tvTitle.updateText(data)
            if (filterProduct.selectedOptions.contains(data)) {
                ivTick.visibility = View.VISIBLE
            } else {
                ivTick.visibility = View.GONE
            }
            itemView.setOnClickListener {
                listener.onSelectOption(filterProduct, position)
            }
        }
    }

    inner class MultiOptionsViewHolder(val binding: ItemProductChooseFilterOptionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val tvTitle = binding.tvOptions

        fun bind(data: String) {
            tvTitle.updateText(data)
            binding.root.setOnClickListener {
                listener.onSelectMultiOptions(filterProduct)
            }
        }
    }
}