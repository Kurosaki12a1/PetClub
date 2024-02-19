package com.kien.petclub.presentation.product.add_info_product

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
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

    companion object {
        private const val TIME_ANIMATION = 300L
    }


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
                        collapseRecyclerView(rvChild, expand)
                    } else {
                        val totalHeightChildRecyclerView =
                            (itemView.height) * data.child!!.size + rvChild.paddingTop
                        expandRecyclerView(totalHeightChildRecyclerView, rvChild, expand)
                    }
                }
                rvChild.layoutManager = LinearLayoutManager(itemView.context)
                rvChild.adapter =
                    SearchInfoProductAdapter(typeInfo, listener).also { it.setData(data.child!!) }
            }

            itemView.setOnLongClickListener {
                showPopup(it, data)
                true
            }

            itemView.setOnClickListener {
                listener?.onClickListener(data)
            }
        }
    }

    private fun expandRecyclerView(height: Int, recyclerView: RecyclerView, view: ImageView) {
        val valueAnimator = ValueAnimator.ofInt(0, height).apply {
            duration = TIME_ANIMATION
            addUpdateListener { animation ->
                interpolator = LinearInterpolator()
                val layoutParams = recyclerView.layoutParams
                layoutParams.height = animation.animatedValue as Int
                recyclerView.layoutParams = layoutParams
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator) {
                    super.onAnimationStart(animation)
                    recyclerView.visibility = View.VISIBLE
                    view.setImageResource(R.drawable.ic_collapse)
                }
            })
        }
        valueAnimator.start()
    }

    private fun collapseRecyclerView(recyclerView: RecyclerView, view: ImageView) {
        val initialHeight = recyclerView.measuredHeight

        val valueAnimator = ValueAnimator.ofInt(initialHeight, 0).apply {
            duration = TIME_ANIMATION
            addUpdateListener { animation ->
                interpolator = LinearInterpolator()
                val layoutParams = recyclerView.layoutParams
                layoutParams.height = animation.animatedValue as Int
                recyclerView.layoutParams = layoutParams
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    view.setImageResource(R.drawable.ic_add)
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

interface SearchInfoProductListener {
    fun onAddInfoProduct(data: InfoProduct)

    fun onDeleteInfoProduct(data: InfoProduct)

    fun onClickListener(data: InfoProduct)
}