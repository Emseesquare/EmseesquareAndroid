package com.fourteen06.emseesquare.presentation.auth

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentProfileSetupBinding
import com.fourteen06.emseesquare.utils.AlertExt.makeShortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSetupFragment : Fragment(R.layout.fragment_profile_setup) {

    val viewModel by viewModels<AuthViewModel>()
    private lateinit var binding: FragmentProfileSetupBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentProfileSetupBinding.bind(view)
        if (savedInstanceState != null) {
            binding.apply {
                idEditText.setText(savedInstanceState.getString(USER_ID_TAG))
                nameEditText.setText(savedInstanceState.getString(USER_NAME_TAG))
                subtitleEditText.setText(savedInstanceState.getString(USER_SUBTITLE_TAG))
                instituteIdEditText.setText(savedInstanceState.getString(USER_INSTITUTE_TAG))
                roles.setText(savedInstanceState.getString(USER_ROLE_TAG))
            }
        }
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val roles = resources.getStringArray(R.array.roles)
        val arrayAdapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, roles)
        binding.roles.setAdapter(arrayAdapter)
        viewModel.events.observe(viewLifecycleOwner) {
            when (it) {
                is AuthOutStates.Error -> {
                    binding.progressBar.visibility = View.GONE
                    makeShortToast(it.message)
                }
                AuthOutStates.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                AuthOutStates.Uninitialized -> {
                    binding.progressBar.visibility = View.GONE

                }
                AuthOutStates.MoveToRootFragment -> {
                    this.binding.progressBar.visibility = View.GONE
                    sendUserToRootFragment()
                }
                AuthOutStates.MoveToUserInfoSetupFragment -> {
                    this.binding.progressBar.visibility = View.GONE
                }
                is AuthOutStates.MoveToOTP_Screen -> {}
                is AuthOutStates.SetProfileImage -> {
                    binding.profileImageView.load(it.imageUrl)
                    this.binding.progressBar.visibility = View.GONE
                }
            }
        }
        val chooserLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it == null) {
                makeShortToast(getString(R.string.no_file_selected))
            } else {
                viewModel.init(AuthInStates.SetUserProfileProfilePhoto(it))
            }
        }
        binding.profileImageView.setOnClickListener {
            chooserLauncher.launch("*/*")
        }
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.generic_tick_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.tick_menu) {
            viewModel.init(
                AuthInStates.SetUserInformation(
                    id = binding.idEditText.text.toString(),
                    name = binding.nameEditText.text.toString(),
                    subtitle = binding.subtitleEditText.text.toString(),
                    instituteId = binding.instituteIdEditText.text.toString(),
                    role = binding.roles.text.toString()
                )
            )
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putString(USER_ID_TAG, binding.idEditText.text.toString())
            putString(USER_NAME_TAG, binding.nameEditText.text.toString())
            putString(USER_SUBTITLE_TAG, binding.subtitleEditText.text.toString())
            putString(USER_INSTITUTE_TAG, binding.instituteIdEditText.text.toString())
            putString(USER_ROLE_TAG, binding.roles.text.toString())
        }
    }

    private fun sendUserToRootFragment() {
        findNavController().navigate(ProfileSetupFragmentDirections.initialToRoot())
    }

    companion object {
        private const val USER_NAME_TAG = "USER_NAME_TAG"
        private const val USER_SUBTITLE_TAG = "USER_SUBTITLE_TAG"
        private const val USER_ID_TAG = "USER_ID_TAG"
        private const val USER_INSTITUTE_TAG = "USER_INSTITUTE_TAG"
        private const val USER_ROLE_TAG = "ROLE_TAG"
    }
}
