package com.kien.petclub.presentation.add_info_product

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
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
    private val typeInfo: String,
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
            if (holder.rvChild.visibility == View.VISIBLE) {
                holder.expand.setImageResource(R.drawable.ic_collapse)
            } else {
                holder.expand.setImageResource(R.drawable.ic_add)
            }
            holder.expand.setOnClickListener {
                if (holder.rvChild.visibility == View.VISIBLE) {
                    holder.expand.setImageResource(R.drawable.ic_add)
                    collapseRecyclerView(holder.rvChild)
                } else {
                    holder.expand.setImageResource(R.drawable.ic_collapse)
                    val totalHeightChildRecyclerView =
                        (holder.itemView.height) * data.child!!.size + holder.rvChild.paddingTop
                    expandRecyclerView(totalHeightChildRecyclerView, holder.rvChild)
                }
            }
            holder.rvChild.layoutManager = LinearLayoutManager(holder.itemView.context)
            holder.rvChild.adapter =
                SearchInfoProductAdapter(typeInfo, listener).also { it.setData(data.child!!) }
        }

        holder.itemView.setOnLongClickListener {
            showPopup(it, data)
            true
        }

        holder.itemView.setOnClickListener {
            listener?.onClickListener(data)
        }
    }

    private fun expandRecyclerView(height: Int, recyclerView: RecyclerView) {
        val valueAnimator = ValueAnimator.ofInt(0, height).apply {
            duration = 1000 // Thời gian animation
            addUpdateListener { animation ->
                val layoutParams = recyclerView.layoutParams
                layoutParams.height = animation.animatedValue as Int
                recyclerView.layoutParams = layoutParams
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                    recyclerView.visibility = View.VISIBLE
                }
            })
        }
        valueAnimator.start()
    }

    private fun collapseRecyclerView(recyclerView: RecyclerView) {
        val initialHeight = recyclerView.measuredHeight

        val valueAnimator = ValueAnimator.ofInt(initialHeight, 0).apply {
            duration = 1000 // Thời gian animation
            addUpdateListener { animation ->
                val layoutParams = recyclerView.layoutParams
                layoutParams.height = animation.animatedValue as Int
                recyclerView.layoutParams = layoutParams
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    recyclerView.visibility = View.GONE
                }
            })
        }
        valueAnimator.start()
    }

    private fun showPopup(v: View, data: InfoProduct) {
        val popUp = PopupMenu(v.context, v)
        val inflater = popUp.menuInflater
        inflater.inflate(R.menu.item_popup_menu, popUp.menu)
        popUp.menu.findItem(R.id.action_add).isVisible =
            typeInfo == VALUE_TYPE && data.parentId == null
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
        val tvInfo = binding.tvInfo
    }

}