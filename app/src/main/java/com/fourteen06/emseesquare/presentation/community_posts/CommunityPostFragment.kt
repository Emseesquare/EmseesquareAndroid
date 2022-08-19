package com.fourteen06.emseesquare.presentation.community_posts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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
import com.fourteen06.emseesquare.utils.getFileName
import com.fourteen06.emseesquare.utils.getMimeType
import com.fourteen06.emseesquare.utils.getTmpFileUri
import com.fourteen06.emseesquare.utils.load
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Integer.max


@AndroidEntryPoint
class CommunityPostFragment : Fragment(
    R.layout.fragment_community_post,
) {
    private val args by navArgs<CommunityPostFragmentArgs>()
    private lateinit var binding: FragmentCommunityPostBinding
    private val viewModel by viewModels<CommunityPostViewModel>()
    private val adapter = CommunityPostAdapter()
    private val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                applyImages()
            } else {
                makeShortToast(getString(R.string.no_file_selected))
                viewModel.init(CommunityPostViewModelInStates.NullifyAttachment)
            }
        }
    private val chooserLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        if (it == null) {
            viewModel.init(CommunityPostViewModelInStates.NullifyAttachment)
            makeShortToast(getString(R.string.no_file_selected))
        } else {
            val uriMime = getMimeType(it)
            val fileType = when {
                uriMime!!.contains("image") -> CommunityPostViewModelInStates.FileType.IMAGE
                uriMime.contains("pdf") -> CommunityPostViewModelInStates.FileType.PDF
                else -> CommunityPostViewModelInStates.FileType.OTHER
            }
            viewModel.init(
                CommunityPostViewModelInStates.SetAttachment(
                    it, getFileName(it), fileType
                )
            )
            applyAttachments(fileType)
        }
    }

    private fun applyImages() {
        binding.included.attachmentWindow.apply {
            viewModel.attachmentUri.observe(viewLifecycleOwner) {
                if (it == null) {
                    binding.included.attachmentExpandableLayout.collapse()

                } else {
                    imageView2.setImageURI(it)
                    binding.included.attachmentExpandableLayout.expand()
                }
            }
            viewModel.fileName.observe(viewLifecycleOwner) {
                textView.text = it.toString()
            }
        }
    }

    private fun applyAttachments(fileType: CommunityPostViewModelInStates.FileType) {
        when (fileType) {
            CommunityPostViewModelInStates.FileType.IMAGE -> applyImages()
            CommunityPostViewModelInStates.FileType.PDF -> {}
            CommunityPostViewModelInStates.FileType.OTHER -> return
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCommunityPostBinding.bind(view)
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
        binding.communityAvatar.load(args.community.communityImage)
        binding.title.text = (args.community.communityName)

        if (args.isEnrolledCommunity) {
            binding.joinCommunityButton.visibility = View.GONE
            binding.included.root.visibility = View.VISIBLE
        } else {
            viewModel.init(CommunityPostViewModelInStates.UnenrolledCommunity)
        }
        binding.postRecyclerView.apply {
            adapter = this@CommunityPostFragment.adapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        viewModel.getCommunityPosts(args.community).observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.postRecyclerView.smoothScrollToPosition(max(0, it.size - 1))

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
                        binding.included.attachmentWindow.progressLoading.visibility =
                            View.INVISIBLE
                        binding.apply {
                            included.messageInput.setText("");
                        }

                    }
                    is CommunityPostViewModelOutStates.ShowToast -> {
                        makeShortToast(it.message)

                    }
                    CommunityPostViewModelOutStates.ShowImageUploading -> {
                        binding.included.attachmentWindow.progressLoading.visibility = View.VISIBLE
                    }
                    CommunityPostViewModelOutStates.ShowLoading -> {

                    }
                }
            }
        }
        binding.included.takePicture.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                getTmpFileUri().let { uri ->
                    val fileName = getFileName(uri)
                    takeImageResult.launch(uri)
                    viewModel.init(CommunityPostViewModelInStates.SetImageUri(uri, fileName))
                }
            }
        }
        binding.included.addAttachment.setOnClickListener {
            chooserLauncher.launch("*/*")
        }
        binding.communityProfileBar.setOnClickListener {
            findNavController().navigate(
                CommunityPostFragmentDirections.actionCommunityPostFragmentToCommunityInfoFragment(
                    args.community
                )
            )
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