package com.kien.petclub.presentation.home

import android.view.MotionEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.kien.imagepicker.extensions.isInVisibleRect
import com.kien.petclub.R
import com.kien.petclub.constants.Constants.TIMEOUT_BACK_PRESS
import com.kien.petclub.constants.Constants.VALUE_GOODS
import com.kien.petclub.constants.Constants.VALUE_SERVICE
import com.kien.petclub.databinding.ActivityHomeBinding
import com.kien.petclub.domain.model.entity.ChooserItem
import com.kien.petclub.domain.model.entity.ProductSortType
import com.kien.petclub.extensions.getVisibleRect
import com.kien.petclub.extensions.setupWithNavController
import com.kien.petclub.extensions.showToast
import com.kien.petclub.presentation.base.BaseActivity
import com.kien.petclub.presentation.product.ShareMultiDataViewModel
import com.kien.petclub.presentation.product.SortProductListener
import com.kien.petclub.presentation.product.goods.GoodsFragment
import com.kien.petclub.presentation.product.goods.GoodsFragmentDirections
import com.kien.petclub.presentation.product.search.SearchFragment
import com.kien.petclub.presentation.product.sort_product.SortChooserPopup
import com.kien.petclub.utils.AnimationLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


/**
 * HomeActivity: An activity class responsible for managing the main navigation and user
 * interactions within the app.
 * This activity utilizes the Jetpack Navigation Component to handle fragment navigations and
 * provides a centralized control for the bottom navigation bar, floating action button (FAB),
 * and animations associated with these UI elements.
 * The activity also manages the back press behavior to either navigate back through the fragments
 * or exit the app.
 * Author: Thinh Huynh
 * Date: 27/02/2024
 */
@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(), SortProductListener {

    companion object {
        private const val TAG_POPUP = "SortChooserPopup"
    }

    private lateinit var popUpSort: SortChooserPopup

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
        popUpSort = SortChooserPopup(ProductSortType.initListChooser(), this)
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

        // Sets up an OnBackPressedCallback to handle the back button press events.
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Checks if there are any fragments in the back stack. If yes, pops the top fragment.
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                    return
                }

                // If back button is pressed twice within TIMEOUT_BACK_PRESS duration, exits the app.
                if (backPressedTime + TIMEOUT_BACK_PRESS > System.currentTimeMillis()) {
                    finish()
                } else {
                    showToast(getString(R.string.press_again_to_exit))
                }

                // Updates the time of the last back press.
                backPressedTime = System.currentTimeMillis()
            }
        })
    }

    /**
     * The purpose of these two functions is to control the visibility of the bottom navigation
     * and FAB based on the application's state. When the user navigates to certain Fragments like
     * GoodsFragment, the bottom navigation and FAB are displayed to enhance interaction and utility.
     * Conversely, when the user is in a Fragment or Activity where these elements are not needed,
     * they are hidden to optimize display space and user experience.
     * */

    // Hides the bottom navigation and the floating action button (FAB) from the view.
    fun hideBottomNavigationAndFabButton() {
        // Notifies the ViewModel to update the FAB state to collapsed
        viewModel.setFabState(false)
        binding.bottomNavigationView.visibility = View.GONE
        binding.actionBtn.visibility = View.GONE
        binding.divider.visibility = View.GONE
    }

    // Shows the bottom navigation and the floating action button (FAB) on the view.
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

    /**
     * Shrinks the floating action button (FAB) back to its original state and
     * hides additional action options.
     */
    private fun shrinkFab() {
        binding.transparentBg.startAnimation(animationLoader.toBottomBgAnim)
        binding.actionBtn.setIconResource(R.drawable.ic_add_btn)
        binding.actionBtn.startAnimation(animationLoader.rotateAntiClockWiseFabAnim)
        binding.addService.startAnimation(animationLoader.toBottomFabAnim)
        binding.addGoods.startAnimation(animationLoader.toBottomFabAnim)

        // Sets the visibility of additional action options and the transparent background to GONE,
        // effectively hiding them.
        binding.addService.visibility = View.GONE
        binding.addGoods.visibility = View.GONE
        binding.tvAddGoods.visibility = View.GONE
        binding.tvAddService.visibility = View.GONE
        binding.transparentBg.visibility = View.GONE
    }

    /**
     * Expands the FAB to show additional action options for adding goods or services.
     */
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

    override fun onSortClick(item: ChooserItem, position: Int) {
        findNavController(R.id.fragment_host_container).currentDestination?.id?.let {
            when (it) {
                R.id.goodsFragment -> {
                    val goodsFragment =
                        supportFragmentManager.findFragmentById(R.id.fragment_host_container)
                    if (goodsFragment is GoodsFragment) {
                        goodsFragment.onSortClick(item, position)
                    }
                }
                R.id.search_fragment -> {
                    val searchFragment =
                        supportFragmentManager.findFragmentById(R.id.fragment_host_container)
                    if (searchFragment is SearchFragment) {
                        searchFragment.onSortClick(item, position)
                    }
                }
            }
        }
    }

    fun showPopUpSort() {
        if (!popUpSort.isVisible) {
            popUpSort.show(supportFragmentManager, TAG_POPUP)
        }
    }

    fun dismissPopUpSort() {
        popUpSort.dismiss()
    }
}