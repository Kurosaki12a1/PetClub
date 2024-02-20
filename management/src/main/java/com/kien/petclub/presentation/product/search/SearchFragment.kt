package com.kien.petclub.presentation.product.search

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.kien.petclub.databinding.FragmentSearchProductBinding
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.presentation.base.BaseFragment
import com.kien.petclub.presentation.home.HomeActivity

class SearchFragment : BaseFragment<FragmentSearchProductBinding>() {
    override fun getViewBinding(): FragmentSearchProductBinding =
        FragmentSearchProductBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    (requireActivity() as HomeActivity).showBottomNavigationAndFabButton()
                    backToPreviousScreen()
                }
            })
    }

    override fun setUpViews() {

    }
}