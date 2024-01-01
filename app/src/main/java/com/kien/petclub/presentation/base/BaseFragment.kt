package com.kien.petclub.presentation.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.kien.petclub.utils.hideLoadingDialog
import com.kien.petclub.utils.showLoadingDialog

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var vb: VB? = null
    open val binding get() = vb!!

    private var mRootView: View? = null
    private var hasInitializedRootView = false
    private var progressDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            initViewBinding(inflater, container)
        }

        return mRootView
    }

    override fun onResume() {
        super.onResume()

        registerListeners()
    }

    override fun onPause() {
        unRegisterListeners()

        super.onPause()
    }

    override
    fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!hasInitializedRootView) {
            getFragmentArguments()
            setBindingVariables()
            setUpViews()
            observeAPICall()
            setupObservers()

            hasInitializedRootView = true
        }
    }


    private fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        vb = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mRootView = binding.root
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun registerListeners() {}

    open fun unRegisterListeners() {}

    open fun getFragmentArguments() {}

    open fun setBindingVariables() {}

    open fun setUpViews() {}

    open fun observeAPICall() {}

    open fun setupObservers() {}

    fun showLoading() {
        hideLoading()
        progressDialog = showLoadingDialog(requireActivity(), null)
    }

    fun showLoading(hint: String?) {
        hideLoading()
        progressDialog = showLoadingDialog(requireActivity(), hint)
    }

    fun hideLoading() = hideLoadingDialog(progressDialog, requireActivity())
}