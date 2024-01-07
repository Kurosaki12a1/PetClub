package com.kien.petclub.presentation.bill

import com.kien.petclub.R
import com.kien.petclub.databinding.FragmentBillBinding
import com.kien.petclub.presentation.base.BaseFragment

class BillFragment : BaseFragment<FragmentBillBinding>() {
    override fun getViewBinding(): FragmentBillBinding = FragmentBillBinding.inflate(layoutInflater)
}