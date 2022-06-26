package com.fourteen06.emseesquare.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentLoginWithPhoneNumberBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_login_with_phone_number) {
    private lateinit var binding: FragmentLoginWithPhoneNumberBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginWithPhoneNumberBinding.bind(view)
    }
}