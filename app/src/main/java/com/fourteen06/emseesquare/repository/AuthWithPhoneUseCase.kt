package com.fourteen06.emseesquare.repository

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@ActivityRetainedScoped
class AuthWithPhoneUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    @Throws(FirebaseException::class)
    suspend operator fun invoke(phoneNumber: String, activity: Activity): PhoneAuthResult =
        suspendCoroutine { cont ->
            val callback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    cont.resume(
                        PhoneAuthResult.VerificationCompleted(credential)
                    )
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    cont.resumeWithException(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    cont.resume(
                        PhoneAuthResult.CodeSent(verificationId, token)
                    )
                }
            }

            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(INDIAN_CODE + phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(callback)
                .setActivity(activity)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
        }

}

sealed class PhoneAuthResult {
    data class VerificationCompleted(val credentials: PhoneAuthCredential) : PhoneAuthResult()
    data class CodeSent(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken
    ) : PhoneAuthResult()
}

private const val INDIAN_CODE = "+91"