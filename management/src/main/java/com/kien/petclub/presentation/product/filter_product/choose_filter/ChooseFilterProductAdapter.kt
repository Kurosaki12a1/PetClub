package com.kien.petclub.presentation.product.filter_product.choose_filter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kien.imagepicker.extensions.collapseItemView
import com.kien.imagepicker.extensions.expandItemView
import com.kien.petclub.R
import com.kien.petclub.databinding.ItemChooseOptionsFilterProductBinding
import com.kien.petclub.domain.model.entity.InfoProductItem
import com.kien.petclub.presentation.product.FilterOptionsListener

class ChooseFilterProductAdapter(
    private val listener: FilterOptionsListener,
) : RecyclerView.Adapter<ChooseFilterProductAdapter.ViewHolder>() {

    companion object {
        private const val ALL_OPTION_INDEX = 0
    }

    private val listInfoProduct = ArrayList<InfoProductItem>()

    private val listRVChildExpanded = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_choose_options_filter_product, parent, false)
        return ViewHolder(ItemChooseOptionsFilterProductBinding.bind(root))
    }

    override fun getItemCount(): Int = listInfoProduct.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            val data = listInfoProduct[position]
            tvInfo.text = data.name
            if (data.isSelected) {
                tvTick.visibility = View.VISIBLE
            } else {
                tvTick.visibility = View.GONE
            }
            if (data.child == null) {
                expand.visibility = View.GONE
                rvChild.visibility = View.GONE
            } else {
                expand.visibility = View.VISIBLE
                if (listRVChildExpanded.contains(position)) {
                    rvChild.visibility = View.VISIBLE
                    expand.setImageResource(R.drawable.ic_collapse)
                } else {
                    rvChild.visibility = View.GONE
                    expand.setImageResource(R.drawable.ic_add)
                }
                expand.setOnClickListener {
                    if (rvChild.visibility == View.VISIBLE) {
                        listRVChildExpanded.remove(position)
                        rvChild.collapseItemView(expand)
                    } else {
                        listRVChildExpanded.add(position)
                        val totalHeightChildRecyclerView =
                            (itemView.height) * data.child!!.size + rvChild.paddingTop
                        rvChild.expandItemView(totalHeightChildRecyclerView, expand)
                    }
                }
                rvChild.layoutManager = LinearLayoutManager(itemView.context)
                rvChild.adapter = ChooseFilterProductAdapter(listener).also {
                    it.setData(data.child!!.toMutableList())
                }
            }
            itemView.setOnClickListener {
                if (position == ALL_OPTION_INDEX && data.parentId == null)
                    listener.onSelectAllOptions(data.isSelected)
                else listener.onSelectItem(data)
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<InfoProductItem>) {
        listInfoProduct.clear()
        listInfoProduct.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemChooseOptionsFilterProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val expand = binding.ivExpand
        val rvChild = binding.rvChild
        val tvInfo = binding.tvInfo
        val tvTick = binding.ivTick
    }
}