package com.fourteen06.emseesquare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.fourteen06.emseesquare.databinding.ActivityMainBinding
import com.fourteen06.emseesquare.presentation.CommunityFragment
import com.fourteen06.emseesquare.presentation.notice.HomeFragment
import com.fourteen06.emseesquare.presentation.message.MessageFragment
import com.fourteen06.emseesquare.presentation.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var messageFragment: MessageFragment
    private lateinit var communitiesFragment: CommunityFragment
    private lateinit var profileFragment: ProfileFragment


    private val fragments: Array<Fragment>
        get() = arrayOf(
            homeFragment,
            messageFragment,
            communitiesFragment,
            profileFragment
        )
    private var selectedIndex = 0

    private val selectedFragment get() = fragments[selectedIndex]

    private fun selectFragment(selectedFragment: Fragment) {
        var transaction = supportFragmentManager.beginTransaction()
        fragments.forEachIndexed { index, fragment ->
            if (selectedFragment == fragment) {
                transaction = transaction.attach(fragment)
                selectedIndex = index
            } else {
                transaction = transaction.detach(fragment)
            }
        }
        transaction.commit()

        title = when (selectedFragment) {
            is HomeFragment -> getString(R.string.title_home)
            is MessageFragment -> getString(R.string.title_message)
            is CommunityFragment -> getString(R.string.title_communities)
            is ProfileFragment -> getString(R.string.title_profile)
            else -> ""
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        if (savedInstanceState == null) {
            homeFragment = HomeFragment()
            messageFragment = MessageFragment()
            communitiesFragment = CommunityFragment()
            profileFragment = ProfileFragment()

            supportFragmentManager.beginTransaction()
                .add(R.id.nav_host_fragment_content_main, homeFragment, TAG_HOME_FRAGMENT)
                .add(
                    R.id.nav_host_fragment_content_main,
                    messageFragment,
                    TAG_MESSAGE_FRAGMENT
                )
                .add(
                    R.id.nav_host_fragment_content_main,
                    communitiesFragment,
                    TAG_COMMUNITIES_FRAGMENT
                )
                .add(R.id.nav_host_fragment_content_main, profileFragment, TAG_PROFILE_FRAGMENT)
                .commit()
        } else {
            homeFragment =
                supportFragmentManager.findFragmentByTag(TAG_HOME_FRAGMENT) as HomeFragment

        }

        selectFragment(selectedFragment)

        binding.appBarMain.btmNavView.setOnNavigationItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.home -> homeFragment
                R.id.message -> messageFragment
                R.id.communities -> communitiesFragment
                R.id.profile -> profileFragment
                else -> throw IllegalArgumentException("Unexpected itemId")
            }

            if (selectedFragment === fragment) {
                if (fragment is OnBottomNavigationFragmentReselectedListener) {
                    fragment.onBottomNavigationFragmentReselected()
                }
            } else {
                selectFragment(fragment)
            }
            true
        }
    }

    interface OnBottomNavigationFragmentReselectedListener {
        fun onBottomNavigationFragmentReselected()
    }

    override fun onBackPressed() {
        if (selectedIndex != 0) {
            binding.appBarMain.btmNavView.selectedItemId = R.id.home
        } else {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_SELECTED_INDEX, selectedIndex)
    }
}

private const val TAG_HOME_FRAGMENT = "TAG_HOME_FRAGMENT"
private const val TAG_MESSAGE_FRAGMENT = "TAG_MESSAGE_FRAGMENT"
private const val TAG_COMMUNITIES_FRAGMENT = "TAG_COMMUNITIES_FRAGMENT"
private const val TAG_PROFILE_FRAGMENT = "TAG_PROFILE_FRAGMENT"
private const val KEY_SELECTED_INDEX = "KEY_SELECTED_INDEX"