package com.kien.petclub.presentation.product.sort_product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kien.petclub.R
import com.kien.petclub.databinding.SortChooseItemBinding
import com.kien.petclub.domain.model.entity.ChooserItem
import com.kien.petclub.presentation.product.SortProductListener

class SortChooseAdapter(
    private val listener: SortProductListener
) : ListAdapter<ChooserItem, SortChooseAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ChooserItem>() {
            override
            fun areItemsTheSame(oldItem: ChooserItem, newItem: ChooserItem): Boolean =
                oldItem.name == newItem.name && oldItem.isSelected == newItem.isSelected

            override
            fun areContentsTheSame(oldItem: ChooserItem, newItem: ChooserItem): Boolean =
                oldItem == newItem
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SortChooseAdapter.ViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.sort_choose_item, parent, false)
        return ViewHolder(SortChooseItemBinding.bind(root))
    }

    override fun onBindViewHolder(holder: SortChooseAdapter.ViewHolder, position: Int) {
        val data = getItem(position)
        holder.itemView.setOnClickListener {
            listener.onSortClick(data, position)
        }
        holder.name.text = data.name
        holder.rb.isChecked = data.isSelected
    }


    inner class ViewHolder(binding: SortChooseItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val rb = binding.rb
        val name = binding.tvName
    }
}
