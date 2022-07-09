package com.fourteen06.emseesquare.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentOtpBinding
import com.fourteen06.emseesquare.utils.AlertExt.makeLongToast
import com.fourteen06.emseesquare.utils.AlertExt.makeShortToast
import com.fourteen06.emseesquare.utils.GenericTextWatcher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpVerifyFragment : Fragment(R.layout.fragment_otp) {
    private lateinit var binding: FragmentOtpBinding
    private val otpVerifyFragmentArgs by navArgs<OtpVerifyFragmentArgs>()
    private val viewModel by activityViewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOtpBinding.bind(view)
        val edit = arrayOf(
            binding.otpEditBox1,
            binding.otpEditBox2,
            binding.otpEditBox3,
            binding.otpEditBox4,
            binding.otpEditBox5,
            binding.otpEditBox6,
        )
        binding.apply {
            otpEditBox1.addTextChangedListener(GenericTextWatcher(otpEditBox1, edit))
            otpEditBox2.addTextChangedListener(GenericTextWatcher(otpEditBox2, edit))
            otpEditBox3.addTextChangedListener(GenericTextWatcher(otpEditBox3, edit))
            otpEditBox4.addTextChangedListener(GenericTextWatcher(otpEditBox4, edit))
            otpEditBox5.addTextChangedListener(GenericTextWatcher(otpEditBox5, edit))
            otpEditBox6.addTextChangedListener(GenericTextWatcher(otpEditBox6, edit))
        }
        binding.checkOtpButton.setOnClickListener {
            val otp = generateOtp()
            if (otp != null) {
                viewModel.init(
                    AuthInStates.VerifyOTP(
                        otp = otp,
                        verificationId = otpVerifyFragmentArgs.verificationToken
                    )
                )
            }
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

                AuthOutStates.Uninitialized -> {
                    this.binding.progressBar.visibility = View.GONE
                }
                is AuthOutStates.MoveToOTP_Screen -> {}
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

    private fun generateOtp(): String? {
        var result = ""
        binding.apply {
            if (otpEditBox1.text.length == 1) {
                result += otpEditBox1.text
            } else {
                makeShortToast("OTP is filled incorrectly.")
                return null
            }

            if (otpEditBox2.text.length == 1) {
                result += otpEditBox2.text
            } else {
                makeShortToast("OTP is filled incorrectly.")
                return null

            }

            if (otpEditBox3.text.length == 1) {
                result += otpEditBox3.text
            } else {
                makeShortToast("OTP is filled incorrectly.")
                return null

            }

            if (otpEditBox4.text.length == 1) {
                result += otpEditBox4.text
            } else {
                makeShortToast("OTP is filled incorrectly.")
                return null

            }

            if (otpEditBox5.text.length == 1) {
                result += otpEditBox5.text
            } else {
                makeShortToast("OTP is filled incorrectly.")
                return null

            }
            if (otpEditBox6.text.length == 1) {
                result += otpEditBox6.text
            } else {
                makeShortToast("OTP is filled incorrectly.")
                return null

            }
            return result
        }

    }

    private fun sendUserToRootFragment() {
        findNavController().navigate(OtpVerifyFragmentDirections.initialToRoot())
    }

    private fun sendUserToProfileSetupFragment() {
        findNavController().navigate(OtpVerifyFragmentDirections.actionGlobalProfileSetupFragment())
    }
}