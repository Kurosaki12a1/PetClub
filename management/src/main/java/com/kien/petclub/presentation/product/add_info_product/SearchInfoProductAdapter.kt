package com.kien.petclub.presentation.product.add_info_product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kien.imagepicker.extensions.collapseItemView
import com.kien.imagepicker.extensions.expandItemView
import com.kien.petclub.R
import com.kien.petclub.constants.Constants.VALUE_TYPE
import com.kien.petclub.databinding.ItemAddInfoProductBinding
import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.presentation.product.InfoProductListener
import com.kien.petclub.presentation.utils.PopupMenuHelper

class SearchInfoProductAdapter(
    private val typeInfo: String,
    private val listener: InfoProductListener? = null
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
        holder.apply {
            tvInfo.text = data.name
            if (data.child == null) {
                expand.visibility = View.GONE
                rvChild.visibility = View.GONE
            } else {
                expand.visibility = View.VISIBLE
                if (rvChild.visibility == View.VISIBLE) {
                    expand.setImageResource(R.drawable.ic_collapse)
                } else {
                    expand.setImageResource(R.drawable.ic_add)
                }
                expand.setOnClickListener {
                    if (rvChild.visibility == View.VISIBLE) {
                        rvChild.collapseItemView(expand)
                    } else {
                        val totalHeightChildRecyclerView =
                            (itemView.height) * data.child!!.size + rvChild.paddingTop
                        rvChild.expandItemView(totalHeightChildRecyclerView, expand)
                    }
                }
                rvChild.layoutManager = LinearLayoutManager(itemView.context)
                rvChild.adapter =
                    SearchInfoProductAdapter(typeInfo, listener).also { it.setData(data.child!!) }
            }

            val popUpHelper = PopupMenuHelper(
                itemView.context,
                R.menu.item_popup_menu, {
                    when (it.itemId) {
                        R.id.action_add -> {
                            listener?.onAddSubInfoProduct(data)
                        }

                        R.id.action_delete -> {
                            listener?.onDeleteInfoProduct(data)
                        }
                    }
                    true
                }, {
                    menu.findItem(R.id.action_add).isVisible =
                        typeInfo == VALUE_TYPE && data.parentId == null
                }
            )

            itemView.setOnLongClickListener {
                popUpHelper.show(it)
                true
            }

            itemView.setOnClickListener {
                listener?.onClickListener(data)
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<InfoProduct>) {
        listInfoProduct.clear()
        listInfoProduct.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemAddInfoProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val expand = binding.ivExpand
        val rvChild = binding.rvChild
        val tvInfo = binding.tvInfo
    }

}