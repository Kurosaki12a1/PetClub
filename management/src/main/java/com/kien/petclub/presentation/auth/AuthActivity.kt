package com.kien.petclub.presentation.auth


import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import com.kien.imagepicker.extensions.initTransitionOpen
import com.kien.petclub.databinding.ActivityAuthBinding
import com.kien.petclub.extensions.openActivity
import com.kien.petclub.presentation.base.BaseActivity
import com.kien.petclub.presentation.home.HomeActivity
import com.kien.petclub.utils.hideSoftInput
import dagger.hilt.android.AndroidEntryPoint

/**
 * AuthActivity: An activity for authentication purposes in the application.
 * @author Thinh Huynh
 * @date 27/02/2024
 */
@AndroidEntryPoint
class AuthActivity : BaseActivity<ActivityAuthBinding>() {
    override fun getViewBinding(): ActivityAuthBinding = ActivityAuthBinding.inflate(layoutInflater)

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            // Check if the focused view is an EditText.
            if (v != null && v is EditText) {
                val outRect = Rect().also { v.getGlobalVisibleRect(it) }
                // If touch is outside the bounds of the EditText, hide the soft input and clear focus.
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