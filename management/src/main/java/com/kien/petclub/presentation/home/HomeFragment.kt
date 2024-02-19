package com.kien.petclub.presentation.home

import com.kien.petclub.databinding.FragmentHomeBinding
import com.kien.petclub.presentation.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)


}