package com.fourteen06.emseesquare.presentation.community

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.viewModels
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentCommunityBinding
import com.fourteen06.emseesquare.utils.MultistackBaseFragment
import com.fourteen06.emseesquare.utils.onQueryTextChanged
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CommunityFragment : MultistackBaseFragment(
    R.layout.fragment_community,
    R.string.title_communities,
    null,
    true,
    true,
    null,
    false,
    null
) {
    private lateinit var searchUserJob: Job
    private val viewModel by viewModels<CommunityViewModel>()
    private val binding by viewBinding(FragmentCommunityBinding::bind)
    private val adapter = CommunityAdapter {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.communityRecyclerView.apply {
            adapter = this@CommunityFragment.adapter
            setHasFixedSize(true)
        }
        viewModel.communityLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.fab.setOnClickListener {
            sendUserToAddCommunity()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.generic_search_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        MenuItemCompat.setOnActionExpandListener(
            searchItem,
            object : MenuItemCompat.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    viewModel.init(CommunityViewModelInState.SearchViewOpen)
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    viewModel.init(CommunityViewModelInState.SearchViewClosed)
                    return true
                }
            })

        searchView.onQueryTextChanged {
            if (this::searchUserJob.isInitialized) {
                searchUserJob.cancel()
            }
            searchUserJob = MainScope().launch {
                delay(500L)
                viewModel.init(CommunityViewModelInState.SearchForCommunity(it))
            }
        }
    }

    private fun sendUserToAddCommunity() {
        findChildNavController().navigate(CommunityFragmentDirections.actionCommunityFragmentToAddCommunityFragment())
    }
}