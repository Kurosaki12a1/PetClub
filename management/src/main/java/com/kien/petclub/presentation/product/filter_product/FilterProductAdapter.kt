package com.kien.petclub.presentation.product.filter_product

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.kien.petclub.R
import com.kien.petclub.data.data_source.local.entity.FilterProductEntity
import com.kien.petclub.databinding.ItemProductFilterBinding
import com.kien.petclub.presentation.product.FilterProductListener

class FilterProductAdapter(
    private val listener: FilterProductListener
) : RecyclerView.Adapter<FilterProductAdapter.ViewHolder>() {

    private val listFilterProduct = ArrayList<FilterProductEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context).inflate(
            R.layout.item_product_filter, parent, false
        )
        return ViewHolder(ItemProductFilterBinding.bind(root))
    }

    override fun getItemCount(): Int = listFilterProduct.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listFilterProduct[position]
        holder.tvTitle.text = data.name
        val adapter = FilterOptionsAdapter(data, listener)
        if (data.isMultiOptions) {
            holder.rvOptions.layoutManager = FlexboxLayoutManager(holder.itemView.context).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
                alignItems = AlignItems.STRETCH
                justifyContent = JustifyContent.FLEX_START
            }
            holder.rvOptions.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val spacing = view.resources.getDimensionPixelSize(R.dimen.dimen5)
                    outRect.left = spacing
                    outRect.right = spacing
                    outRect.bottom = spacing
                    outRect.top = spacing
                }
            })
            holder.ivAction.visibility = View.VISIBLE
            // Work around for click listener
            holder.ivAction.setOnClickListener {
                listener.onSelectMultiOptions(data)
            }
        } else {
            holder.ivAction.visibility = View.GONE
            holder.rvOptions.layoutManager = LinearLayoutManager(holder.itemView.context)
        }

        holder.rvOptions.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<FilterProductEntity>) {
        listFilterProduct.clear()
        listFilterProduct.addAll(data)
        notifyDataSetChanged()
    }

    fun updateData(data: FilterProductEntity) {
        val index = listFilterProduct.indexOf(data)
        listFilterProduct[index] = data
        notifyItemChanged(index)
    }

    inner class ViewHolder(binding: ItemProductFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvTitle = binding.tvFilterOptions
        val rvOptions = binding.rvFilterOptions
        val ivAction = binding.ivAction
    }
}