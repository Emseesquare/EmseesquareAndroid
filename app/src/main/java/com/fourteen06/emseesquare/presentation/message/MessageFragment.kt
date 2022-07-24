package com.fourteen06.emseesquare.presentation.message

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentMessageBinding
import com.fourteen06.emseesquare.presentation.RootFragmentDirections
import com.fourteen06.emseesquare.utils.AlertExt.makeShortToast
import com.fourteen06.emseesquare.utils.MultistackBaseFragment
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.onQueryTextChanged
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MessageFragment : MultistackBaseFragment(
    R.layout.fragment_message,
    R.string.title_message,
    null,
    true,
    true,
    null,
    false,
    null
) {
    private val binding by viewBinding(FragmentMessageBinding::bind)
    val searchAdapter = UserMessageRoomInviteAdapter {
        viewModel.init(MessageViewmodelInStates.MakeNewChatRoom(it.uid))
    }
    val messageAdapter = MessageAdapter(Firebase.auth.currentUser?.uid.toString()) {
        (parentFragment?.parentFragment)?.findNavController()
            ?.navigate(RootFragmentDirections.actionRootFragmentToChatFragment(it))
    }
    private val viewModel by viewModels<MessageViewModel>()
    private lateinit var searchUserJob: Job
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.adapterChangeIndicator.observe(viewLifecycleOwner) {
            when (it) {
                true -> {
                    binding.messageRecyclerView.apply {
                        adapter = this@MessageFragment.searchAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }
                false -> {
                    binding.messageRecyclerView.apply {
                        adapter = this@MessageFragment.messageAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                }
            }
        }
        viewModel.userFlow.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    makeShortToast(it.message)
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    searchAdapter.submitList(it.data)
                }
            }
        }
        viewModel.messageRoomFlow.observe(viewLifecycleOwner) {
            messageAdapter.submitList(it)
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
                    viewModel.init(MessageViewmodelInStates.ShowSearchAdapter)
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    viewModel.init(MessageViewmodelInStates.ShowMessageAdapter)

                    return true
                }
            })

        searchView.onQueryTextChanged {
            if (this::searchUserJob.isInitialized) {
                searchUserJob.cancel()
            }
            searchUserJob = MainScope().launch {
                delay(500L)
                viewModel.init(MessageViewmodelInStates.FindUserByName(it))
            }
        }
    }
}