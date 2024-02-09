package com.kien.petclub.presentation.add_info_product

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.kien.petclub.R
import com.kien.petclub.constants.Constants.DATA
import com.kien.petclub.constants.Constants.EMPTY_STRING
import com.kien.petclub.constants.Constants.KEY_TYPE
import com.kien.petclub.constants.Constants.VALUE_BRAND
import com.kien.petclub.constants.Constants.VALUE_LOCATION
import com.kien.petclub.constants.Constants.VALUE_TYPE
import com.kien.petclub.databinding.ActivityAddInfoProductBinding
import com.kien.petclub.domain.model.entity.InfoProduct
import com.kien.petclub.domain.util.Resource
import com.kien.petclub.extensions.initTransitionClose
import com.kien.petclub.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddInfoProductActivity : BaseActivity<ActivityAddInfoProductBinding>(),
    SearchInfoProductListener {

    private lateinit var adapter: SearchInfoProductAdapter

    private lateinit var popUp: AddInfoProductPopup

    private val viewModel: AddInfoProductViewModel by viewModels()

    private var parentTypeId = EMPTY_STRING

    private var typeAddInfo = EMPTY_STRING

    override fun getViewBinding(): ActivityAddInfoProductBinding =
        ActivityAddInfoProductBinding.inflate(layoutInflater)

    override fun setUpViews() {
        super.setUpViews()

        typeAddInfo = intent?.getStringExtra(KEY_TYPE) ?: EMPTY_STRING
        popUp = AddInfoProductPopup(typeAddInfo)

        viewModel.getInfo(typeAddInfo)

        adapter = SearchInfoProductAdapter(typeAddInfo, this)
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = adapter

        binding.ivBack.setOnClickListener {
            initTransitionClose()
            finish()
        }

        binding.ivAdd.setOnClickListener {
            showAddPopUp()
        }

        when (typeAddInfo) {
            VALUE_BRAND -> {
                binding.title.text = getString(R.string.choose_brand)
                binding.etSearch.hint = getString(R.string.name_brand)
            }

            VALUE_LOCATION -> {
                binding.title.text = getString(R.string.choose_location)
                binding.etSearch.hint = getString(R.string.name_location)
            }

            VALUE_TYPE -> {
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

    override fun setUpObserver() {
        super.setUpObserver()
        viewModel.addResponse.onEach {
            when (it) {
                is Resource.Success -> {
                    stopLoadingAnimation()
                    // After added, we continue update recycler view
                    viewModel.getInfo(typeAddInfo)
                }

                is Resource.Loading -> {
                    showLoadingAnimation()
                }

                else -> {
                    stopLoadingAnimation()
                }
            }
        }.launchIn(lifecycleScope)

        viewModel.getResponse.onEach {
            when (it) {
                is Resource.Success -> {
                    stopLoadingAnimation()
                    adapter.setData(it.value)
                }

                is Resource.Loading -> {
                    showLoadingAnimation()
                }

                else -> {
                    stopLoadingAnimation()
                }
            }

        }.launchIn(lifecycleScope)


        val popUpViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        lifecycleScope.launch {
            popUpViewModel.dataPopup.collect {
                if (it.isNotBlank() && it.isNotEmpty()) {
                    if (parentTypeId != EMPTY_STRING && typeAddInfo == VALUE_TYPE) {
                        viewModel.updateTypeProduct(parentTypeId, it)
                        parentTypeId = EMPTY_STRING
                    } else {
                        viewModel.addInfo(typeAddInfo, it)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.searchResponse.collectLatest {
                when (it) {
                    is Resource.Success -> {
                        stopLoadingAnimation()
                        adapter.setData(it.value)
                    }
                    is Resource.Loading -> {
                        showLoadingAnimation()
                    }

                    else -> {
                        stopLoadingAnimation()
                    }
                }
            }
        }
    }

    private fun showAddPopUp() {
        popUp.show(supportFragmentManager, "AddInfoProductPopup")
    }

    private fun showLoadingAnimation() {
        binding.loadingAnimationView.visibility = View.VISIBLE
        binding.loadingAnimationView.playAnimation()
    }

    private fun stopLoadingAnimation() {
        binding.loadingAnimationView.visibility = View.GONE
        binding.loadingAnimationView.cancelAnimation()
    }


    override fun onAddInfoProduct(data: InfoProduct) {
        // If this is child of type, we need pass parent id
        if (typeAddInfo == VALUE_TYPE && data.parentId == null) {
            parentTypeId = data.id
        }
        showAddPopUp()
    }

    override fun onDeleteInfoProduct(data: InfoProduct) {

    }

    override fun onClickListener(data: InfoProduct) {
        // TODO Need pass data of parent if this is child of type
        val intent = Intent()
        intent.putExtra(DATA, data.name)
        intent.putExtra(KEY_TYPE, typeAddInfo)
        setResult(Activity.RESULT_OK, intent)
        initTransitionClose()
        finish()
    }


}