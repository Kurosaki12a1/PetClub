package com.kien.petclub.presentation.goods

import com.kien.petclub.databinding.FragmentGoodsBinding
import com.kien.petclub.presentation.base.BaseFragment

class GoodsFragment : BaseFragment<FragmentGoodsBinding>() {
    override fun getViewBinding(): FragmentGoodsBinding =
        FragmentGoodsBinding.inflate(layoutInflater)
}