package com.kien.petclub.presentation.product.sort_product

import android.graphics.Color
import android.graphics.Insets
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.databinding.SortChooserPopUpBinding
import com.kien.petclub.domain.model.entity.ChooserItem
import com.kien.petclub.presentation.product.common.ProductListener

class SortChooserPopup(
    private val listItem: ArrayList<ChooserItem>,
    private val listener: ProductListener
) : DialogFragment() {

    private lateinit var binding: SortChooserPopUpBinding

    private lateinit var adapter: SortChooseAdapter

    override fun getTheme(): Int = R.style.SortDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SortChooserPopUpBinding.inflate(layoutInflater)
        dialog?.apply {
            window?.attributes?.gravity = Gravity.BOTTOM
            window?.attributes?.windowAnimations = R.style.SortDialogFragment
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(true)
            window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun getHeightWindow(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics =
                requireActivity().windowManager.currentWindowMetrics
            val insets: Insets = windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            return windowMetrics.bounds.height() - insets.top -
                    insets.bottom

        } else {
            val window = dialog!!.window
            val size = Point()
            // Store dimensions of the screen in `size`
            val display = window!!.windowManager.defaultDisplay
            display.getSize(size)
            return size.y
        }
    }

    override fun onResume() {
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                (getHeightWindow() * 0.4).toInt()
            )
            setGravity(Gravity.BOTTOM)
        }
        super.onResume()
    }

    private fun setUpViews() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        adapter = SortChooseAdapter(object : ProductListener {
            override fun onItemClick(item: ChooserItem, position: Int) {
                super.onItemClick(item, position)
                listItem.forEach { it.isSelected = false }
                item.isSelected = true
                listener.onItemClick(item, position)
            }
        })

        binding.rvSortChooser.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSortChooser.adapter = adapter.apply {
            submitList(listItem)
        }
    }
}