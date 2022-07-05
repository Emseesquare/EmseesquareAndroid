package com.fourteen06.emseesquare.multistack_fragments.root.first

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FirstFragment1Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class First1Fragment : Fragment(R.layout.first_fragment_1) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FirstFragment1Binding.bind(view)

        binding.buttonFirstFragment1GoTo2.setOnClickListener {
            NavHostFragment.findNavController(requireParentFragment())
                .navigate(R.id.first1_to_first2)
        }
    }
}