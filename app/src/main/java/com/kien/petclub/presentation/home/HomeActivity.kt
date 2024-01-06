package com.kien.petclub.presentation.home

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ListPopupWindow
import android.widget.PopupMenu
import android.widget.PopupWindow
import com.kien.petclub.R
import com.kien.petclub.databinding.ActivityHomeBinding
import com.kien.petclub.extensions.setupWithNavController
import com.kien.petclub.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    override fun getLayoutId(): Int = R.layout.activity_home

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
        setUpImageSwitcher()
    }

    // When click item Menu, it will open new Activity with Fragment.
    private fun setUpImageSwitcher() {
        binding.actionBtn.apply {
            inAnimation = AnimationUtils.loadAnimation(this@HomeActivity, R.anim.anim_in)
            inAnimation = AnimationUtils.loadAnimation(this@HomeActivity, R.anim.anim_out)
            setImageResource(R.drawable.ic_add_btn)
            setOnClickListener {
                setImageResource(R.drawable.ic_cancel)
               /* val popMenu = PopupMenu(this@HomeActivity, this)
                popMenu.menuInflater.inflate(R.menu.menu_add, popMenu.menu)
                popMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.add_pet -> {
                            navController.navigate(R.id.action_global_addPetFragment)
                            true
                        }
                        R.id.add_bill -> {
                            navController.navigate(R.id.action_global_addBillFragment)
                            true
                        }
                        R.id.add_goods -> {
                            navController.navigate(R.id.action_global_addGoodsFragment)
                            true
                        }
                        else -> false
                    }
                }*/
            }
        }
    }


  /*  private fun setUpImageSwitcher() {
        binding.actionBtn.apply {
            inAnimation = AnimationUtils.loadAnimation(this@HomeActivity, R.anim.anim_in)
            inAnimation = AnimationUtils.loadAnimation(this@HomeActivity, R.anim.anim_out)
            setImageResource(R.drawable.ic_add_btn)
            setOnClickListener {
                setImageResource(R.drawable.ic_cancel)
            }
        }
    }*/
}