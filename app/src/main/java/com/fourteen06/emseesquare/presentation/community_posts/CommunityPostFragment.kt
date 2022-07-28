package com.fourteen06.emseesquare.presentation.community_posts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentCommunityPostBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CommunityPostFragment : Fragment(
    R.layout.fragment_community_post,
) {
    private val args by navArgs<CommunityPostFragmentArgs>()
    private val binding by viewBinding(FragmentCommunityPostBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            this.binding.included.chatEditText.setText(
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
            this.binding.included.chatEditText.text.toString()
        )
    }

    companion object {
        private val MESSAGE_EDIT_TEXT_TAG = "MESSAGE_EDIT_TEXT_TAG"
    }
}