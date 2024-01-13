package com.kien.petclub.presentation.home

import android.graphics.Rect
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kien.petclub.R
import com.kien.petclub.databinding.ActivityHomeBinding
import com.kien.petclub.extensions.setupWithNavController
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
    }

    private fun setUpFloatingActionButton() {
        binding.actionBtn.setIconResource(R.drawable.ic_add_btn)
        binding.actionBtn.setOnClickListener { viewModel.updateFabState() }
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
            val outRect = Rect()
            binding.actionBtn.getGlobalVisibleRect(outRect)
            viewModel.shrinkFab(ev, outRect)
        }
        return super.dispatchTouchEvent(ev)
    }


}