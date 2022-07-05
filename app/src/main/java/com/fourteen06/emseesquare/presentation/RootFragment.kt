package com.fourteen06.emseesquare.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentRootBinding
import com.fourteen06.emseesquare.utils.FragmentStackHostFragment
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RootFragment : Fragment(R.layout.fragment_root) {
    private val binding by viewBinding(FragmentRootBinding::bind)

    private lateinit var homeFragmentHost: FragmentStackHostFragment
    private lateinit var messageFragmentHost: FragmentStackHostFragment
    private lateinit var communityFragmentHost: FragmentStackHostFragment
    private lateinit var profileFragmentHost: FragmentStackHostFragment

    private val fragments: Array<Fragment>
        get() = arrayOf(
            homeFragmentHost,
            messageFragmentHost,
            communityFragmentHost,
            profileFragmentHost
        )
    private var selectedIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            val homeFragment =
                FragmentStackHostFragment.newInstance(R.navigation.home_nav_graph)
                    .also { this.homeFragmentHost = it }
            val messageFragment =
                FragmentStackHostFragment.newInstance(R.navigation.message_nav_graph)
                    .also { this.messageFragmentHost = it }
            val communityFragment =
                FragmentStackHostFragment.newInstance(R.navigation.community_nav_graph)
                    .also { this.communityFragmentHost = it }
            val profileFragment =
                FragmentStackHostFragment.newInstance(R.navigation.profile_nav_graph)
                    .also { this.profileFragmentHost = it }
            childFragmentManager.beginTransaction()
                .add(R.id.containerBottomNavContent, homeFragment, HOME_FRAGMENT)
                .add(R.id.containerBottomNavContent, messageFragment, MESSAGE_FRAGMENT)
                .add(R.id.containerBottomNavContent, communityFragment, COMMUNITY_FRAGMENT)
                .add(R.id.containerBottomNavContent, profileFragment, PROFILE_FRAGMENT)
                .selectFragment(selectedIndex)
                .commit()
        } else {
            selectedIndex = savedInstanceState.getInt(SELECTED_INDEX, 0)

            homeFragmentHost =
                childFragmentManager.findFragmentByTag(HOME_FRAGMENT) as FragmentStackHostFragment
            messageFragmentHost =
                childFragmentManager.findFragmentByTag(MESSAGE_FRAGMENT) as FragmentStackHostFragment
            communityFragmentHost =
                childFragmentManager.findFragmentByTag(COMMUNITY_FRAGMENT) as FragmentStackHostFragment
            profileFragmentHost =
                childFragmentManager.findFragmentByTag(PROFILE_FRAGMENT) as FragmentStackHostFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        tabs.forEachIndexed { index, textView ->
//            textView.setOnClickListener {
//                selectFragment(index)
//            }
//        }
        binding.appBarMain.btmNavView.apply {
            setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.home -> {
                        selectFragment(0)
                        true
                    }
                    R.id.message -> {
                        selectFragment(1)
                        true
                    }
                    R.id.communities -> {
                        selectFragment(2)
                        true
                    }
                    R.id.profile -> {
                        selectFragment(3)
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SELECTED_INDEX, selectedIndex)
    }

    private fun FragmentTransaction.selectFragment(selectedIndex: Int): FragmentTransaction {
        fragments.forEachIndexed { index, fragment ->
            if (index == selectedIndex) {
                attach(fragment)
            } else {
                detach(fragment)
            }
        }
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        return this
    }


    private fun selectFragment(indexToSelect: Int) {
        this.selectedIndex = indexToSelect
        childFragmentManager.beginTransaction()
            .selectFragment(indexToSelect)
            .commit()
    }
}

private const val SELECTED_INDEX = "SELECTED_INDEX"
private const val HOME_FRAGMENT = "HOME_FRAGMENT"
private const val MESSAGE_FRAGMENT = "MESSAGE_FRAGMENT"
private const val COMMUNITY_FRAGMENT = "COMMUNITY_FRAGMENT"
private const val PROFILE_FRAGMENT = "PROFILE_FRAGMENT"
