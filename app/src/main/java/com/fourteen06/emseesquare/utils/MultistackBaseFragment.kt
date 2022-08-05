package com.fourteen06.emseesquare.utils

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.addCallback
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.MenuRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.controller.DrawerController

open class MultistackBaseFragment(
    @LayoutRes val contentLayoutId: Int,
    @StringRes val titleRes: Int?,
    val title: String?,
    private val isTopLevelFragment: Boolean,
    private val showHomeAsUp: Boolean,
    @DrawableRes private val homeIcon: Int?,
    private val hasMenu: Boolean,
    @MenuRes private val menuRes: Int?
) : Fragment(contentLayoutId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (hasMenu && menuRes == null) {
            throw IllegalStateException(MENU_ILLEGAL_STATE_MESSAGE)
        }
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isTopLevelFragment) {
            (parentFragment!!.parentFragment!!.parentFragment!! as DrawerController).unlockDrawer()
        } else {
            (parentFragment!!.parentFragment!!.parentFragment!! as DrawerController).lockDrawer()

        }
        (activity as AppCompatActivity).apply {
            if (this@MultistackBaseFragment.titleRes != null) {

                title = getString(this@MultistackBaseFragment.titleRes)
            }
            if (this@MultistackBaseFragment.title != null) {
                title = this@MultistackBaseFragment.title
            }
            if (showHomeAsUp) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                if (homeIcon != null) {
                    supportActionBar?.setHomeAsUpIndicator(homeIcon)
                } else {
                    if (isTopLevelFragment) {
                        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_hamburger_icon)
                    } else {
                        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
                        (requireActivity() as OnBackPressedDispatcherOwner).onBackPressedDispatcher.addCallback(
                            viewLifecycleOwner
                        ) {
                            findChildNavController()
                                .popBackStack()
                        }
                    }
                }
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (!hasMenu) {
            super.onCreateOptionsMenu(menu, inflater)
        } else {
            inflater.inflate(menuRes!!, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (isTopLevelFragment) {
                (parentFragment!!.parentFragment!!.parentFragment!! as DrawerController).openDrawer()
            } else {
                //act as backbutton
                findChildNavController().popBackStack()
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun findChildNavController(): NavController {
        return NavHostFragment.findNavController(requireParentFragment())
    }

    companion object {
        private const val MENU_ILLEGAL_STATE_MESSAGE = "MENU_ILLEGAL_STATE_MESSAGE"
    }
}