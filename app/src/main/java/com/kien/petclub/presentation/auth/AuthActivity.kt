package com.kien.petclub.presentation.auth


import android.graphics.Rect
import android.view.MotionEvent
import android.widget.EditText
import com.kien.petclub.databinding.ActivityAuthBinding
import com.kien.petclub.presentation.base.BaseActivity
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

}