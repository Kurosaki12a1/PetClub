package com.kien.petclub.presentation.add_info_product

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.kien.petclub.R
import com.kien.petclub.constants.Constants.EMPTY_STRING
import com.kien.petclub.constants.Constants.VALUE_BRAND
import com.kien.petclub.constants.Constants.VALUE_TYPE
import com.kien.petclub.databinding.ViewPopUpBinding

class AddInfoProductPopup(private val typeInfoProduct: String) : DialogFragment() {

    private lateinit var viewModel: SharedViewModel

    private lateinit var binding: ViewPopUpBinding

    override fun getTheme(): Int = R.style.PrettyDialogAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ViewPopUpBinding.inflate(layoutInflater)
        dialog?.apply {
            window?.attributes?.gravity = Gravity.CENTER
            window?.requestFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(true)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    override fun onResume() {
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setGravity(Gravity.CENTER)
        }
        super.onResume()
        binding.etAdd.setText(EMPTY_STRING)
    }

    private fun setUpViews() {
        setUpTitle()
        binding.tvCancel.setOnClickListener {
            dismiss()
        }

        binding.tvSubmit.setOnClickListener {
            viewModel.setData(binding.etAdd.text.toString())
            dismiss()
        }

    }

    private fun setUpTitle() {
        when (typeInfoProduct) {
            VALUE_BRAND -> binding.tvTitle.text = getString(R.string.add_brand)
            VALUE_TYPE -> binding.tvTitle.text = getString(R.string.add_type)
            else -> binding.tvTitle.text = getString(R.string.add_location)
        }
    }


}