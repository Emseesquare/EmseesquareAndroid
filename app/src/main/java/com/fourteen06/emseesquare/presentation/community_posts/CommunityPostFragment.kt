package com.fourteen06.emseesquare.presentation.community_posts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentCommunityPostBinding
import com.fourteen06.emseesquare.utils.AlertExt.makeShortToast
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Integer.max


@AndroidEntryPoint
class CommunityPostFragment : Fragment(
    R.layout.fragment_community_post,
) {
    private val args by navArgs<CommunityPostFragmentArgs>()
    private val binding by viewBinding(FragmentCommunityPostBinding::bind)
    private val viewModel by viewModels<CommunityPostViewModel>()
    private val adapter = CommunityPostAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            this.binding.included.messageInput.setText(
                savedInstanceState.getString(
                    MESSAGE_EDIT_TEXT_TAG
                )
            )
        }
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        binding.title.text = (args.community.communityName)
        binding.postRecyclerView.apply {
            adapter = this@CommunityPostFragment.adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        viewModel.getCommunityPosts(args.community).observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.included.voiceRecordingOrSend.setOnClickListener {
            viewModel.init(
                CommunityPostViewModelInStates.SendMessage(
                    this.binding.included.messageInput.text.toString(),
                    this.args.community
                )
            )
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collect {
                when (it) {
                    CommunityPostViewModelOutStates.MessageSendSuccessfully -> {
                        binding.apply {
                            included.messageInput.setText("");
                        }

                    }
                    is CommunityPostViewModelOutStates.ShowToast -> {
                        makeShortToast(it.message)

                    }
                }
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            //act as backbutton
            findNavController().popBackStack()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        inflater.inflate(R.menu.community_post_toolbar_menu, menu)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            MESSAGE_EDIT_TEXT_TAG,
            this.binding.included.messageInput.text.toString()
        )
    }

    companion object {
        private val MESSAGE_EDIT_TEXT_TAG = "MESSAGE_EDIT_TEXT_TAG"
    }
}