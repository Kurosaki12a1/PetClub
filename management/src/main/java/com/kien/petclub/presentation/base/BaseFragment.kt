package com.kien.petclub.presentation.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding


abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var vb: VB? = null
    open val binding get() = vb!!

    abstract fun getViewBinding(): VB

    private var mRootView: View? = null
    private var hasInitializedRootView = false
    private var progressDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRootView == null) {
            vb = getViewBinding()
            mRootView = vb?.root
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

    open fun registerListeners() {}

    open fun unRegisterListeners() {}

    open fun getFragmentArguments() {}

    open fun setBindingVariables() {}

    open fun setUpViews() {}

    open fun observeAPICall() {}

    open fun setupObservers() {}

}