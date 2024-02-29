package com.kien.petclub.presentation.product.add_info_product

import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.constants.Constants
import com.kien.petclub.constants.Constants.TAG_ADD_INFO_PRODUCT_POPUP
import com.kien.petclub.databinding.FragmentAddInfoProductBinding
import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.backToPreviousScreen
import com.kien.petclub.presentation.base.BaseFragment
import com.kien.petclub.presentation.product.InfoProductListener
import com.kien.petclub.presentation.product.ShareMultiDataViewModel
import com.kien.petclub.presentation.product.utils.hideLoadingAnimation
import com.kien.petclub.presentation.product.utils.showDialog
import com.kien.petclub.presentation.product.utils.showLoadingAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddInfoProductFragment : BaseFragment<FragmentAddInfoProductBinding>(),
    InfoProductListener {
    private lateinit var adapter: SearchInfoProductAdapter

    private lateinit var dialog: AddInfoProductPopup

    private val viewModel: AddInfoProductViewModel by viewModels()

    private val sharedVM: ShareMultiDataViewModel by activityViewModels()

    private var parentTypeId = Constants.EMPTY_STRING

    private var typeAddInfo = Constants.EMPTY_STRING

    override fun getViewBinding(): FragmentAddInfoProductBinding =
        FragmentAddInfoProductBinding.inflate(layoutInflater)

    override fun setUpViews() {
        super.setUpViews()

        typeAddInfo = arguments?.getString(Constants.KEY_TYPE) ?: Constants.EMPTY_STRING
        dialog = AddInfoProductPopup(typeAddInfo)

        viewModel.getInfo(typeAddInfo)

        adapter = SearchInfoProductAdapter(typeAddInfo, this)
        binding.rvList.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvList.adapter = adapter

        binding.ivBack.setOnClickListener {
            backToPreviousScreen()
        }

        binding.ivAdd.setOnClickListener {
            showDialog(dialog, TAG_ADD_INFO_PRODUCT_POPUP)
        }

        when (typeAddInfo) {
            Constants.VALUE_BRAND -> {
                binding.title.text = getString(R.string.choose_brand)
                binding.etSearch.hint = getString(R.string.name_brand)
            }

            Constants.VALUE_LOCATION -> {
                binding.title.text = getString(R.string.choose_location)
                binding.etSearch.hint = getString(R.string.name_location)
            }

            Constants.VALUE_TYPE -> {
                binding.title.text = getString(R.string.choose_type)
                binding.etSearch.hint = getString(R.string.name_type)
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isBlank()) viewModel.getInfo(typeAddInfo)
                    else viewModel.searchInfo(typeAddInfo, s.toString())
                }
            }
        })
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.addResponse.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is Resource.Success -> {
                    hideLoadingAnimation()
                    // After added, we continue update recycler view
                    viewModel.getInfo(typeAddInfo)
                }

                is Resource.Loading -> {
                    showLoadingAnimation()
                }

                else -> {
                    hideLoadingAnimation()
                }
            }
        }.launchIn(lifecycleScope)

        viewModel.getResponse.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is Resource.Success -> {
                    hideLoadingAnimation()
                    adapter.setData(it.value)
                }

                is Resource.Loading -> {
                    showLoadingAnimation()
                }

                else -> {
                    hideLoadingAnimation()
                }
            }

        }.launchIn(lifecycleScope)

        viewModel.deleteResponse.flowWithLifecycle(lifecycle).onEach {
            when (it) {
                is Resource.Success -> {
                    // After deleted, we continue update recycler view
                    viewModel.getInfo(typeAddInfo)
                }

                is Resource.Loading -> {
                    showLoadingAnimation()
                }

                else -> {
                    hideLoadingAnimation()
                }
            }

        }.launchIn(lifecycleScope)


        lifecycleScope.launch {
            sharedVM.infoProductResponse.flowWithLifecycle(lifecycle).collect {
                if (!it.isNullOrBlank() && it.isNotEmpty()) {
                    if (parentTypeId != Constants.EMPTY_STRING && typeAddInfo == Constants.VALUE_TYPE) {
                        viewModel.updateTypeProduct(parentTypeId, it)
                        parentTypeId = Constants.EMPTY_STRING
                    } else {
                        viewModel.addInfo(typeAddInfo, it)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.searchResponse.flowWithLifecycle(lifecycle).collectLatest {
                when (it) {
                    is Resource.Success -> {
                        hideLoadingAnimation()
                        adapter.setData(it.value)
                    }

                    is Resource.Loading -> {
                        showLoadingAnimation()
                    }

                    else -> {
                        hideLoadingAnimation()
                    }
                }
            }
        }
    }

    override fun onAddInfoProduct(data: InfoProduct) {
        // If this is child of type, we need pass parent id
        if (typeAddInfo == Constants.VALUE_TYPE && data.parentId == null) {
            parentTypeId = data.id
        }
        showDialog(dialog, TAG_ADD_INFO_PRODUCT_POPUP)
    }

    override fun onDeleteInfoProduct(data: InfoProduct) {
        viewModel.deleteInfo(typeAddInfo, data.id, data.parentId)
    }

    override fun onClickListener(data: InfoProduct) {
        sharedVM.setInfoProduct(data.name)
        backToPreviousScreen()
    }

}