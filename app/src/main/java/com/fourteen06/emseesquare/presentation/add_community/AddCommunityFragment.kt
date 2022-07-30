package com.fourteen06.emseesquare.presentation.add_community

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentAddNoticeBinding
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.utils.AlertExt.makeShortToast
import com.fourteen06.emseesquare.utils.MultistackBaseFragment
import com.fourteen06.emseesquare.utils.load
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCommunityFragment : MultistackBaseFragment(
    R.layout.fragment_add_notice,
    R.string.title_add_community,
    null,
    false,
    true,
    null,
    true,
    R.menu.generic_tick_menu
) {
    private val binding by viewBinding(FragmentAddNoticeBinding::bind)
    private val viewModel by viewModels<AddCommunityViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            binding.editText.setText(
                savedInstanceState.getString(
                    DESCRIPTION_TEXT_KEY,
                    ""
                )
            )

        }
        restoreAttachment()
        val chooserLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it == null) {
                makeShortToast(getString(R.string.no_file_selected))
            } else {
                viewModel.init(AddCommunityViewModelInStates.UploadFileButtonClicked(it))
            }
        }
        binding.uploadButton.setOnClickListener {
            chooserLauncher.launch("*/*")
        }
        viewModel.events.asLiveData(viewLifecycleOwner.lifecycleScope.coroutineContext)
            .observe(viewLifecycleOwner) {
                when (it) {
                    is AddCommunityViewModelOutStates.AttachmentLoadedSuccessful -> {
                        binding.attachmentProgressBar.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.INVISIBLE
                        viewModel.init(AddCommunityViewModelInStates.SaveAttachment(it.attachmentType))
                    }
                    AddCommunityViewModelOutStates.AttachmentLoading -> {
                        binding.attachmentProgressBar.visibility = View.VISIBLE
                    }
                    AddCommunityViewModelOutStates.CommunityAddedSuccessful -> {
                        binding.attachmentProgressBar.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.INVISIBLE
                        findChildNavController().popBackStack()
                    }
                    AddCommunityViewModelOutStates.CommunityAddingLoading -> {
                        binding.attachmentProgressBar.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is AddCommunityViewModelOutStates.ErrorOccured -> {
                        makeShortToast(it.message)
                        binding.attachmentProgressBar.visibility = View.INVISIBLE
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }
            }
    }

    private fun restoreAttachment() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.attachment.collect {
                when (it) {
                    is AttachmentType.File -> {

                    }
                    is AttachmentType.Image -> {
                        binding.contentThumnails.apply {
                            load(it.imageUrl)
                            visibility = View.VISIBLE
                        }
                        binding.contentThumnailsPlaceholder.visibility = View.GONE
                    }
                    AttachmentType.None -> {
                        binding.contentThumnails.apply {
                            visibility = View.GONE
                        }
                        binding.contentThumnailsPlaceholder.visibility = View.VISIBLE

                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            DESCRIPTION_TEXT_KEY,
            this.binding.editText.text.toString()
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.tick_menu) {
            viewModel.init(AddCommunityViewModelInStates.AddNewCommunity(binding.editText.text.toString()))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

private const val DESCRIPTION_TEXT_KEY = "DESCRIPTION_TEXT_KEY"
