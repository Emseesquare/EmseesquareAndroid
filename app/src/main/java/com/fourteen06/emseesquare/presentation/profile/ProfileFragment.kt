package com.fourteen06.emseesquare.presentation.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentProfileBinding
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



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            appDatastoreManager.getCurrentUser().collect {
//                binding.profileImageView.load(it.profileImageUrl) {
//                    transformations(CircleCropTransformation())
//                }
//                binding.userNameTextView.text = it.name
//                binding.subtitleTextView.text = it.subTitle
//            }
        }
    }
}