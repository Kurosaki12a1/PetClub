package com.kien.petclub.presentation.auth


import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.kien.petclub.databinding.ActivityAuthBinding
import com.kien.petclub.extensions.initTransitionOpen
import com.kien.petclub.extensions.openActivity
import com.kien.petclub.presentation.base.BaseActivity
import com.kien.petclub.presentation.home.HomeActivity
import com.kien.petclub.utils.hideSoftInput
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {
    override fun getViewBinding(): ActivityAuthBinding = ActivityAuthBinding.inflate(layoutInflater)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v != null && v is EditText) {
                val outRect = Rect().also { v.getGlobalVisibleRect(it) }
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    hideSoftInput(this)
                    v.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun showLoadingAnimation() {
        binding.loadingAnimationView.visibility = View.VISIBLE
        binding.loadingAnimationView.playAnimation()
    }

    fun stopLoadingAnimation() {
        binding.loadingAnimationView.visibility = View.GONE
        binding.loadingAnimationView.cancelAnimation()
    }

    fun openHome() {
        openActivity(HomeActivity::class.java)
        initTransitionOpen()
        finish()
    }

}