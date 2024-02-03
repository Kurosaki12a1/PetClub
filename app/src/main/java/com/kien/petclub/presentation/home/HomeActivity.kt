package com.kien.petclub.presentation.home

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kien.petclub.R
import com.kien.petclub.constants.Constants.KEY_TYPE
import com.kien.petclub.constants.Constants.TIMEOUT_BACK_PRESS
import com.kien.petclub.constants.Constants.VALUE_GOODS
import com.kien.petclub.constants.Constants.VALUE_SERVICE
import com.kien.petclub.databinding.ActivityHomeBinding
import com.kien.petclub.extensions.getVisibleRect
import com.kien.petclub.extensions.isInVisibleRect
import com.kien.petclub.extensions.openActivity
import com.kien.petclub.extensions.setupWithNavController
import com.kien.petclub.extensions.showToast
import com.kien.petclub.presentation.add_product.AddProductActivity
import com.kien.petclub.presentation.base.BaseActivity
import com.kien.petclub.utils.AnimationLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    private val viewModel by viewModels<HomeViewModel>()

    override fun getViewBinding() = ActivityHomeBinding.inflate(layoutInflater)

    private val animationLoader = AnimationLoader(this)

    override fun setUpBottomNavigation() {
        setUpBottomNavigationWithGraphs()
    }

    private fun setUpBottomNavigationWithGraphs() {
        val graphIds = listOf(
            R.navigation.nav_home,
            R.navigation.nav_bill,
            R.navigation.nav_goods,
            R.navigation.nav_notifications,
            R.navigation.nav_more
        )

        val controller = binding.bottomNavigationView.setupWithNavController(
            graphIds,
            supportFragmentManager,
            R.id.fragment_host_container,
            intent
        )

        binding.bottomNavigationView.setOnItemSelectedListener {
            if (it.itemId == R.id.nav_goods) {
                binding.actionBtn.visibility = View.VISIBLE
            } else {
                binding.actionBtn.visibility = View.GONE
            }
            true
        }

        navController = controller
    }

    override fun setUpViews() {
        super.setUpViews()
        setUpFloatingActionButton()
    }

    override fun setUpObserver() {
        viewModel.fabState.onEach { isExtended ->
            if (isExtended) {
                expandFab()
            } else {
                shrinkFab()
            }
        }.launchIn(lifecycleScope)

        var backPressedTime = 0L

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + TIMEOUT_BACK_PRESS > System.currentTimeMillis()) {
                    finish()
                } else {
                    showToast(getString(R.string.press_again_to_exit))
                }
                backPressedTime = System.currentTimeMillis()
            }
        })
    }

    private fun setUpFloatingActionButton() {
        binding.actionBtn.setIconResource(R.drawable.ic_add_btn)
        binding.actionBtn.setOnClickListener { viewModel.updateFabState() }

        binding.addService.setOnClickListener {
            openActivity(
                AddProductActivity::class.java,
                KEY_TYPE to VALUE_SERVICE
            )
        }

        binding.tvAddService.setOnClickListener {
            openActivity(
                AddProductActivity::class.java,
                KEY_TYPE to VALUE_SERVICE
            )
        }

        binding.addGoods.setOnClickListener {
            openActivity(
                AddProductActivity::class.java,
                KEY_TYPE to VALUE_GOODS
            )
        }

        binding.tvAddGoods.setOnClickListener {
            openActivity(
                AddProductActivity::class.java,
                KEY_TYPE to VALUE_GOODS
            )
        }
    }

    private fun shrinkFab() {
        binding.transparentBg.startAnimation(animationLoader.toBottomBgAnim)
        binding.actionBtn.setIconResource(R.drawable.ic_add_btn)
        binding.actionBtn.startAnimation(animationLoader.rotateAntiClockWiseFabAnim)
        binding.addService.startAnimation(animationLoader.toBottomFabAnim)
        binding.addGoods.startAnimation(animationLoader.toBottomFabAnim)

        binding.addService.visibility = View.GONE
        binding.addGoods.visibility = View.GONE
        binding.tvAddGoods.visibility = View.GONE
        binding.tvAddService.visibility = View.GONE
        binding.transparentBg.visibility = View.GONE
    }

    private fun expandFab() {
        binding.addService.visibility = View.VISIBLE
        binding.addGoods.visibility = View.VISIBLE
        binding.tvAddGoods.visibility = View.VISIBLE
        binding.tvAddService.visibility = View.VISIBLE
        binding.transparentBg.visibility = View.VISIBLE

        binding.transparentBg.startAnimation(animationLoader.fromBottomBgAnim)
        binding.actionBtn.setIconResource(R.drawable.ic_cancel)
        binding.actionBtn.startAnimation(animationLoader.rotateClockWiseFabAnim)
        binding.addService.startAnimation(animationLoader.fromBottomFabAnim)
        binding.addGoods.startAnimation(animationLoader.fromBottomFabAnim)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            if (binding.addService.isInVisibleRect(ev.rawX.toInt(), ev.rawY.toInt()) ||
                binding.addGoods.isInVisibleRect(ev.rawX.toInt(), ev.rawY.toInt()) ||
                binding.tvAddService.isInVisibleRect(ev.rawX.toInt(), ev.rawY.toInt()) ||
                binding.tvAddGoods.isInVisibleRect(ev.rawX.toInt(), ev.rawY.toInt())
            ) {
                return super.dispatchTouchEvent(ev)
            }

            viewModel.shrinkFab(ev, binding.actionBtn.getVisibleRect())
        }
        return super.dispatchTouchEvent(ev)
    }
}