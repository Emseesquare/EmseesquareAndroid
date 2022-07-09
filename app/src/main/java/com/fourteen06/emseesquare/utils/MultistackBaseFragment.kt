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

open class MultistackBaseFragment(
    @LayoutRes val contentLayoutId: Int,
    @StringRes val title: Int,
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
        (activity as AppCompatActivity).apply {
            title = getString(this@MultistackBaseFragment.title)
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
                            findNavController()
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
                //act as menu button

            } else {
                //act as backbutton
                findNavController().popBackStack()
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun findNavController(): NavController {
        return NavHostFragment.findNavController(requireParentFragment())
    }

    companion object {
        private const val MENU_ILLEGAL_STATE_MESSAGE = "MENU_ILLEGAL_STATE_MESSAGE"
    }
}