package com.fourteen06.emseesquare.models

import android.os.Parcelable
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthNavArgs(
    val verificationId: String,
    val phoneNumber: String,
    val token: PhoneAuthProvider.ForceResendingToken
) : Parcelable