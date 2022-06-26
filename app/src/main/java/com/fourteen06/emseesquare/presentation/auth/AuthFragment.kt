package com.fourteen06.emseesquare.presentation.auth

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentLoginWithPhoneNumberBinding
import com.fourteen06.emseesquare.utils.AlertExt.makeLongToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_login_with_phone_number) {
    private lateinit var binding: FragmentLoginWithPhoneNumberBinding
    private val viewModel by activityViewModels<AuthViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginWithPhoneNumberBinding.bind(view)
        if (savedInstanceState != null) {
            binding.phoneNumberEditText.setText(savedInstanceState.getString(PHONE_NUMBER))
        }
        binding.verifyPhoneNumberButton.setOnClickListener {
            viewModel.init(
                AuthInStates.LoginWithPhoneNumber(
                    binding.phoneNumberEditText.text.toString(),
                    activity as Activity
                )
            )
        }
        viewModel.events.observe(viewLifecycleOwner) {
            when (it) {
                is AuthOutStates.Error -> {
                    this.binding.progressBar.visibility = View.GONE
                    makeLongToast(it.message)
                }
                AuthOutStates.Loading -> {
                    this.binding.progressBar.visibility = View.VISIBLE
                }
                is AuthOutStates.MoveToOTP_Screen -> {
                    this.binding.progressBar.visibility = View.GONE
                    moveUserToOTP_Screen()
                }
                AuthOutStates.Success -> {
                    this.binding.progressBar.visibility = View.GONE
                    moveUserToHomeScreen()
                }
                AuthOutStates.Uninitialized -> {
                    this.binding.progressBar.visibility = View.GONE

                }
            }
        }
    }

    private fun moveUserToHomeScreen() {
        makeLongToast("moveUserToHomeScreen")
    }

    private fun moveUserToOTP_Screen() {
        makeLongToast("moveUserToOTP_Screen")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PHONE_NUMBER, binding.phoneNumberEditText.text.toString())
    }
}

private const val PHONE_NUMBER = "PHONE_NUMBER"