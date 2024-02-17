package com.kien.petclub.presentation.goods

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.databinding.FragmentGoodsBinding
import com.kien.petclub.domain.model.entity.Product
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.presentation.base.BaseFragment
import com.kien.petclub.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GoodsFragment : BaseFragment<FragmentGoodsBinding>(), GoodsListener {

    private val viewModel: GoodsViewModel by viewModels()

    private lateinit var adapter: GoodsAdapter
    override fun getViewBinding(): FragmentGoodsBinding =
        FragmentGoodsBinding.inflate(layoutInflater)

    override fun setUpViews() {
        adapter = GoodsAdapter(this)
        binding.rvProduct.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvProduct.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllProduct()
    }

    override fun setupObservers() {
        super.setupObservers()
        lifecycleScope.launch {
            viewModel.productResponse.collect {
                when (it) {
                    is Resource.Success -> {
                        adapter.setData(it.value)
                        (requireActivity() as HomeActivity).stopLoadingAnimation()
                    }

                    is Resource.Loading -> {
                        (requireActivity() as HomeActivity).showLoadingAnimation()
                    }

                    is Resource.Failure -> {
                        (requireActivity() as HomeActivity).stopLoadingAnimation()
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onItemClick(product: Product) {
        lifecycleScope.launch {
            val act = requireActivity() as HomeActivity
            act.setDataToSharedVM(product)
            (requireActivity() as HomeActivity).navigateToDetailProduct()
        }
    }

}