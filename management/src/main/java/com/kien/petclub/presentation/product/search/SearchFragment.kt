package com.kien.petclub.presentation.product.search

import com.kien.petclub.databinding.FragmentSearchProductBinding
import com.kien.petclub.presentation.base.BaseFragment

class SearchFragment : BaseFragment<FragmentSearchProductBinding>() {
    override fun getViewBinding(): FragmentSearchProductBinding =
        FragmentSearchProductBinding.inflate(layoutInflater)

}