package com.fourteen06.emseesquare.presentation.auth

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentLoginWithPhoneNumberBinding
import com.fourteen06.emseesquare.repository.utils.AppSharedPreference
import com.fourteen06.emseesquare.utils.AlertExt.makeLongToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_login_with_phone_number) {
    private lateinit var binding: FragmentLoginWithPhoneNumberBinding
    private val viewModel by activityViewModels<AuthViewModel>()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var sharedPref: AppSharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (sharedPref.getUserStatus() == AppSharedPreference.CurrentStatus.REGISTERED) {
            sendUserToRootFragment()
        } else if (sharedPref.getUserStatus() == AppSharedPreference.CurrentStatus.LOGGED_IN) {
            sendUserToProfileSetupFragment()
        }
    }

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
                    moveUserToOTP_Screen(it.verificationId, it.token)
                }

                AuthOutStates.Uninitialized -> {
                    this.binding.progressBar.visibility = View.GONE

                }
                AuthOutStates.MoveToRootFragment -> {
                    this.binding.progressBar.visibility = View.GONE
                    sendUserToRootFragment()
                }
                AuthOutStates.MoveToUserInfoSetupFragment -> {
                    this.binding.progressBar.visibility = View.GONE
                    sendUserToProfileSetupFragment()
                }

            }
        }
    }

    private fun moveUserToOTP_Screen(
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
        findNavController().navigate(
            AuthFragmentDirections.actionAuthFragmentToOtpVerifyFragment(
                verificationId,
                token,
                this.binding.phoneNumberEditText.text.toString()
            )
        )
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(PHONE_NUMBER, binding.phoneNumberEditText.text.toString())
    }

    private fun sendUserToRootFragment() {
        findNavController().navigate(AuthFragmentDirections.initialToRoot())
    }

    private fun sendUserToProfileSetupFragment() {
        findNavController().navigate(AuthFragmentDirections.actionGlobalProfileSetupFragment())
    }
}

private const val PHONE_NUMBER = "PHONE_NUMBER"
