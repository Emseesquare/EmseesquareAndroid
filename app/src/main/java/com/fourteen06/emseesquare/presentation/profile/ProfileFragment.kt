package com.fourteen06.emseesquare.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentProfileBinding
import com.fourteen06.emseesquare.repository.utils.AppSharedPreference
import com.fourteen06.emseesquare.utils.MultistackBaseFragment
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : MultistackBaseFragment(
    R.layout.fragment_profile,
    R.string.title_profile,
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
                profileImageView.load(currentUser.profileImageUrl) {
                    transformations(CircleCropTransformation())
                }
                userNameTextView.setText(currentUser.name)
                subtitleTextView.setText(currentUser.subTitle)
            }

        }
    }
}