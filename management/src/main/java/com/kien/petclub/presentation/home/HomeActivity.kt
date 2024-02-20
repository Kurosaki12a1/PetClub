package com.kien.petclub.presentation.home

import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.kien.petclub.R
import com.kien.petclub.constants.Constants.TIMEOUT_BACK_PRESS
import com.kien.petclub.constants.Constants.VALUE_GOODS
import com.kien.petclub.constants.Constants.VALUE_SERVICE
import com.kien.petclub.databinding.ActivityHomeBinding
import com.kien.petclub.extensions.getVisibleRect
import com.kien.petclub.extensions.initTransitionOpen
import com.kien.petclub.extensions.isInVisibleRect
import com.kien.petclub.extensions.openActivity
import com.kien.petclub.extensions.setupWithNavController
import com.kien.petclub.extensions.showToast
import com.kien.petclub.presentation.base.BaseActivity
import com.kien.petclub.presentation.product.goods.GoodsFragmentDirections
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
        ) {
            if (it.itemId == R.id.nav_goods) {
                binding.actionBtn.visibility = View.VISIBLE
            } else {
                binding.actionBtn.visibility = View.GONE
            }
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

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
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

    fun hideBottomNavigationAndFabButton() {
        viewModel.setFabState(false) // When open something then we must always set this to false
        binding.bottomNavigationView.visibility = View.GONE
        binding.actionBtn.visibility = View.GONE
        binding.divider.visibility = View.GONE
    }

    fun showBottomNavigationAndFabButton() {
        binding.bottomNavigationView.visibility = View.VISIBLE
        binding.actionBtn.visibility = View.VISIBLE
        binding.divider.visibility = View.VISIBLE
    }

    private fun openProductView(view: View, productType: String) {
        view.setOnClickListener {
            findNavController(R.id.fragment_host_container).navigate(
                GoodsFragmentDirections.actionOpenAddFragment(productType)
            )
        }
    }

    private fun setUpFloatingActionButton() {
        binding.actionBtn.setIconResource(R.drawable.ic_add_btn)
        binding.actionBtn.setOnClickListener { viewModel.updateFabState() }
        openProductView(binding.addService, VALUE_SERVICE)
        openProductView(binding.tvAddService, VALUE_SERVICE)
        openProductView(binding.addGoods, VALUE_GOODS)
        openProductView(binding.tvAddGoods, VALUE_GOODS)
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
        if (ev?.action == MotionEvent.ACTION_DOWN && binding.actionBtn.visibility == View.VISIBLE) {
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

    fun showLoadingAnimation() {
        binding.loadingAnimationView.visibility = View.VISIBLE
        binding.loadingAnimationView.playAnimation()
    }

    fun stopLoadingAnimation() {
        binding.loadingAnimationView.visibility = View.GONE
        binding.loadingAnimationView.cancelAnimation()
    }

}