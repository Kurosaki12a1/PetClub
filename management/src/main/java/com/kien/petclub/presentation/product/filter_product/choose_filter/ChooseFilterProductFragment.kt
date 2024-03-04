package com.kien.petclub.presentation.product.filter_product.choose_filter

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.constants.Constants
import com.kien.petclub.constants.Constants.DATA
import com.kien.petclub.constants.Constants.KEY_FILTER
import com.kien.petclub.constants.Constants.KEY_INFO_PRODUCT
import com.kien.petclub.data.data_source.local.entity.FilterProductEntity
import com.kien.petclub.data.mapper.toInfoProductItem
import com.kien.petclub.databinding.FragmentChooseFilterProductBinding
import com.kien.petclub.domain.model.entity.InfoProductItem
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.presentation.base.BaseFragment
import com.kien.petclub.presentation.home.HomeActivity
import com.kien.petclub.presentation.product.FilterOptionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChooseFilterProductFragment : BaseFragment<FragmentChooseFilterProductBinding>(),
    FilterOptionsListener {
    companion object {
        const val INDEX_OPTION_ALL = 0
    }

    private var typeInfo: String = ""

    private var listInfoProductItem = ArrayList<InfoProductItem>()

    private var filterProduct: FilterProductEntity? = null

    private lateinit var listFilterChosen: ArrayList<String>

    private lateinit var adapter: ChooseFilterProductAdapter

    private val viewModel: ChooseFilterProductViewModel by viewModels()
    override fun getViewBinding(): FragmentChooseFilterProductBinding =
        FragmentChooseFilterProductBinding.inflate(layoutInflater)

    override fun setUpViews() {
        super.setUpViews()

        arguments?.let {
            filterProduct = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(DATA, FilterProductEntity::class.java)
            } else {
                it.getParcelable(DATA)
            }
            listFilterChosen = filterProduct?.selectedOptions ?: arrayListOf()
            typeInfo = it.getString(KEY_INFO_PRODUCT) ?: Constants.EMPTY_STRING
            viewModel.getInfoProduct(typeInfo)
        }

        binding.ivBack.setOnClickListener { backToPreviousScreen() }

        binding.ivApply.setOnClickListener {
            filterProduct?.apply {
                selectedOptions = listFilterChosen
                options = listFilterChosen
            }
            setFragmentResult(KEY_FILTER, Bundle().apply {
                putParcelable(DATA, filterProduct)
            })
            backToPreviousScreen()
        }
        adapter = ChooseFilterProductAdapter(this)
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setUpDataRecyclerView(list: ArrayList<String>, data: List<InfoProductItem>) {
        if (list.isNotEmpty()) {
            if (listFilterChosen.contains(Constants.NAME_OPTIONS_ALL)) {
                data.forEach {
                    it.isSelected = true
                    it.child?.forEach { child -> child.isSelected = true }
                }
            } else {
                data.forEach { item ->
                    item.isSelected = list.contains(item.fullName) || list.contains(item.name)
                    item.child?.forEach {
                        child -> child.isSelected =
                        list.contains(child.fullName) || list.contains(child.name)
                    }
                }
            }
        }
        listInfoProductItem.addAll(data)
        adapter.setData(data)
        binding.rvList.adapter = adapter
    }

    override fun setupObservers() {
        super.setupObservers()
        lifecycleScope.launch {
            viewModel.getInfoProduct.flowWithLifecycle(lifecycle).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        (requireActivity() as HomeActivity).stopLoadingAnimation()
                        if (listInfoProductItem.isEmpty()) {
                            val data = it.value.map { item -> item.toInfoProductItem() }
                            setUpDataRecyclerView(listFilterChosen, data)
                        }
                    }

                    is Resource.Failure -> {
                        (requireActivity() as HomeActivity).stopLoadingAnimation()
                    }

                    is Resource.Loading -> {
                        (requireActivity() as HomeActivity).showLoadingAnimation()
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onSelectAllOptions(isSelected: Boolean) {
        listInfoProductItem.forEach {
            it.isSelected = !isSelected
            it.child?.forEach { child -> child.isSelected = !isSelected }
        }
        listFilterChosen.clear()
        if (!isSelected) {
            // Instead of get all data, we just add "Tất cả" option for simple
            listFilterChosen.add(listInfoProductItem[INDEX_OPTION_ALL].fullName)
        }
        adapter.setData(listInfoProductItem)
    }

    override fun onSelectItem(item: InfoProductItem) {
        viewModel.executeItemData(listInfoProductItem, item)
        viewModel.executeFilterChosen(listFilterChosen, listInfoProductItem)
        adapter.setData(listInfoProductItem)
    }


}