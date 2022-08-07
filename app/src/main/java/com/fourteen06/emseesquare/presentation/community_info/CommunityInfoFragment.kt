package com.fourteen06.emseesquare.presentation.community_info

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentCommunityInfoBinding
import com.fourteen06.emseesquare.presentation.message.UserAdapter
import com.fourteen06.emseesquare.utils.load
import com.fourteen06.emseesquare.utils.onQueryTextChanged
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommunityInfoFragment : Fragment(R.layout.fragment_community_info) {
    private lateinit var binding: FragmentCommunityInfoBinding
    private val viewModel by viewModels<CommunityInfoViewModel>()
    private val args by navArgs<CommunityInfoFragmentArgs>()
    private var searchDrawable: Drawable? = null
    private var toolbarExpandStatus: Boolean = true
    private lateinit var searchUserJob: Job
    private val adapter = UserAdapter {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            toolbarExpandStatus = savedInstanceState.getBoolean(TOOLBAR_EXPAND_STATUS, true)
        }
        binding = FragmentCommunityInfoBinding.bind(view)
        setupToolbar()
        binding.memberRecyclerView.apply {
            this.adapter = this@CommunityInfoFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
        viewModel.getUserFlow(args.community.communityId).observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        setHasOptionsMenu(true)
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            title = (args.community.communityName)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding.communityAvatar.load(args.community.communityImage)
        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout: AppBarLayout, offset: Int ->
            val value = 1 - offset.toFloat() / appBarLayout.totalScrollRange * -1
            val colorComponent = Math.max(0.5f, value)
            binding.toolbar.navigationIcon?.colorFilter =
                PorterDuffColorFilter(
                    Color.rgb(colorComponent, colorComponent, colorComponent),
                    PorterDuff.Mode.SRC_ATOP
                )
            searchDrawable?.colorFilter =
                PorterDuffColorFilter(
                    Color.rgb(colorComponent, colorComponent, colorComponent),
                    PorterDuff.Mode.SRC_ATOP
                )
        })
        binding.appBar.setExpanded(this.toolbarExpandStatus)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.generic_search_menu, menu)
        this.searchDrawable = menu.getItem(0).icon
        if (searchDrawable != null) {
            searchDrawable?.mutate()
        }
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        MenuItemCompat.setOnActionExpandListener(
            searchItem,
            object : MenuItemCompat.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    this@CommunityInfoFragment.toolbarExpandStatus = false
                    binding.appBar.setExpanded(this@CommunityInfoFragment.toolbarExpandStatus)
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    viewModel.init(CommunityInfoViewModelInState.NullifySearchQuery)
                    return true
                }
            })

        searchView.onQueryTextChanged { name ->
            if (this::searchUserJob.isInitialized) {
                searchUserJob.cancel()
            }
            searchUserJob = MainScope().launch {
                delay(500L)
                viewModel.init(CommunityInfoViewModelInState.FindUserByName(name))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(TOOLBAR_EXPAND_STATUS, this.toolbarExpandStatus)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TOOLBAR_EXPAND_STATUS = "TOOLBAR_COLLAPSE_STATUS"
    }
}