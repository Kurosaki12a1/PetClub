package com.kien.petclub.presentation.product.filter_product

import android.os.Build
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.constants.Constants
import com.kien.petclub.data.data_source.local.entity.FilterProductEntity
import com.kien.petclub.databinding.FragmentFilterProductBinding
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.extensions.navigateSafe
import com.kien.petclub.presentation.base.BaseFragment
import com.kien.petclub.presentation.home.HomeActivity
import com.kien.petclub.presentation.product.FilterProductListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilterProductFragment : BaseFragment<FragmentFilterProductBinding>(), FilterProductListener {

    private lateinit var adapter: FilterProductAdapter

    private val viewModel: FilterProductViewModel by viewModels()

    private val listFilterProduct = ArrayList<FilterProductEntity>()
    override fun getViewBinding(): FragmentFilterProductBinding =
        FragmentFilterProductBinding.inflate(layoutInflater)


    override fun setUpViews() {
        super.setUpViews()
        (requireActivity() as HomeActivity).hideBottomNavigationAndFabButton()
        binding.ivBack.setOnClickListener { backToPreviousScreen() }
        binding.tvApply.setOnClickListener {
            viewModel.insertListFilterProduct(listFilterProduct)
        }

        binding.rvFilter.layoutManager = LinearLayoutManager(requireContext())
        // Avoid blinking when notifyItemChanged
        binding.rvFilter.itemAnimator = null
        adapter = FilterProductAdapter(this)

        parentFragmentManager.setFragmentResultListener(
            Constants.KEY_FILTER,
            viewLifecycleOwner,
        ) { _, result ->
            val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.getParcelable(Constants.DATA, FilterProductEntity::class.java)
            } else {
                result.getParcelable(Constants.DATA)
            }
            if (data != null && listFilterProduct.isNotEmpty()) {
                val index = listFilterProduct.indexOfFirst { it.id == data.id }
                listFilterProduct[index] = data
                adapter.updateData(data)
            }
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        lifecycleScope.launch {
            viewModel.allOptionsFilter.flowWithLifecycle(lifecycle).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        (requireActivity() as HomeActivity).stopLoadingAnimation()
                        if (!it.value.isNullOrEmpty()) {
                            adapter.setData(it.value)
                            listFilterProduct.clear()
                            listFilterProduct.addAll(it.value)
                            binding.rvFilter.adapter = adapter
                        }
                    }
                    is Resource.Loading -> {
                        (requireActivity() as HomeActivity).showLoadingAnimation()
                    }

                    else -> {}
                }
            }
        }

        lifecycleScope.launch {
            viewModel.insertFilterResponse.flowWithLifecycle(lifecycle).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        (requireActivity() as HomeActivity).stopLoadingAnimation()
                        backToPreviousScreen()
                    }
                    is Resource.Loading -> {
                        (requireActivity() as HomeActivity).showLoadingAnimation()
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onSelectOption(filterProduct: FilterProductEntity, position: Int) {
        val data = filterProduct.options[position]
        filterProduct.selectedOptions.clear()
        filterProduct.selectedOptions.add(data)
        val index = listFilterProduct.indexOfFirst { it.id == filterProduct.id }
        listFilterProduct[index] = filterProduct
        adapter.updateData(filterProduct)
    }

    override fun onSelectMultiOptions(filterProduct: FilterProductEntity) {
        navigateSafe(
            FilterProductFragmentDirections.actionOpenChooseFilterProductFragment(
                filterProduct,
                when (filterProduct.name) {
                    Constants.NAME_TYPE_PRODUCT -> Constants.VALUE_TYPE
                    Constants.NAME_BRAND_PRODUCT -> Constants.VALUE_BRAND
                    Constants.NAME_LOCATION_PRODUCT -> Constants.VALUE_LOCATION
                    else -> Constants.EMPTY_STRING
                }
            )
        )
    }
}