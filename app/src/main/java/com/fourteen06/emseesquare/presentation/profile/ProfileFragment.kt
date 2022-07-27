package com.fourteen06.emseesquare.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentProfileBinding
import com.fourteen06.emseesquare.repository.utils.AppSharedPreference
import com.fourteen06.emseesquare.utils.MultistackBaseFragment
import com.fourteen06.emseesquare.utils.loadProfileImage
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : MultistackBaseFragment(
    R.layout.fragment_profile,
    R.string.title_profile,
    null,
    true,
    true,
    null,
    false,
    null
) {
    private val binding by viewBinding(FragmentProfileBinding::bind)

    @Inject
    lateinit var appSharedPreference: AppSharedPreference
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            val currentUser = appSharedPreference.getUser()
            binding.apply {
                profileImageView.loadProfileImage(currentUser.profileImageUrl)
                userNameTextView.setText(currentUser.name)
                subtitleTextView.setText(currentUser.subTitle)
            }

        }
    }
}