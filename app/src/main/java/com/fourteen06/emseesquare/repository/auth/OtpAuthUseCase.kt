package com.fourteen06.emseesquare.repository.auth

import com.fourteen06.emseesquare.repository.utils.AppDatastoreManager
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@ActivityRetainedScoped
class OtpAuthUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val appDatastoreManager: AppDatastoreManager
) {
    @Throws(FirebaseException::class)
    suspend operator fun invoke(otp: String, verificationId: String): OtpAuthResult {
        try {
            val credential =
                verificationId.let { PhoneAuthProvider.getCredential(it, otp) }
            val result = firebaseAuth.signInWithCredential(credential).await()
            if (result != null) {
                if (result.user != null) {
                    appDatastoreManager.setCurrentStatus(AppDatastoreManager.CurrentStatus.LOGGED_IN)
                    return OtpAuthResult.Success(result.user!!)
                } else {
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        appDatastoreManager.setCurrentStatus(AppDatastoreManager.CurrentStatus.LOGGED_IN)
                        return OtpAuthResult.Success(user)
                    }
                }
            }
        } catch (e: FirebaseException) {
            return OtpAuthResult.Error(e);
        }
        return OtpAuthResult.UnknownError;

    }

}

sealed class OtpAuthResult {
    object UnknownError : OtpAuthResult()
    data class Success(val user: FirebaseUser) : OtpAuthResult()
    data class Error(val exception: FirebaseException) : OtpAuthResult()
}