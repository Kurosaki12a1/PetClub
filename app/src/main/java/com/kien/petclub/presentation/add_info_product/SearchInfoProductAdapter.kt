package com.kien.petclub.presentation.add_info_product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kien.petclub.R
import com.kien.petclub.constants.Constants.VALUE_TYPE
import com.kien.petclub.databinding.ItemAddInfoProductBinding
import com.kien.petclub.domain.model.entity.InfoProduct

class SearchInfoProductAdapter(
    private val typeInfo : String,
    private val listener: SearchInfoProductListener? = null
) : RecyclerView.Adapter<SearchInfoProductAdapter.ViewHolder>() {

    private var listInfoProduct = ArrayList<InfoProduct>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_add_info_product, parent, false)
        return ViewHolder(ItemAddInfoProductBinding.bind(root))
    }

    override fun getItemCount(): Int = listInfoProduct.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listInfoProduct[position]
        holder.tvInfo.text = data.name
        if (data.child == null) {
            holder.expand.visibility = View.GONE
            holder.rvChild.visibility = View.GONE
        } else {
            holder.expand.visibility = View.VISIBLE
            holder.expand.setOnClickListener {
                if (holder.rvChild.visibility == View.VISIBLE) {
                    holder.rvChild.visibility = View.GONE
                } else {
                    holder.rvChild.visibility = View.VISIBLE
                }
            }
            holder.rvChild.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.rvChild.adapter =
                SearchInfoProductAdapter(typeInfo, listener).also { it.setData(data.child!!) }
        }

        holder.parentView.setOnLongClickListener {
            showPopup(it, data)
            true
        }
        holder.tvInfo.setOnClickListener {
            listener?.onClickListener(data)
        }

    }

    private fun showPopup(v : View, data: InfoProduct) {
        val popUp = PopupMenu(v.context, v)
        val inflater = popUp.menuInflater
        inflater.inflate(R.menu.item_popup_menu, popUp.menu)
        popUp.menu.findItem(R.id.action_add).isVisible = typeInfo != VALUE_TYPE
        popUp.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add -> {
                    listener?.onAddInfoProduct(data)
                    true
                }
                R.id.action_delete -> {
                    listener?.onDeleteInfoProduct(data)
                    true
                }
                else -> false
            }
        }
        popUp.show()
    }

    fun setData(data: ArrayList<InfoProduct>) {
        listInfoProduct.clear()
        listInfoProduct.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemAddInfoProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val expand = binding.ivExpand
        val rvChild = binding.rvChild
        val parentView = binding.itemParent
        val tvInfo = binding.tvInfo
    }

}