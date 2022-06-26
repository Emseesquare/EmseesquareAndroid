package com.fourteen06.emseesquare.presentation.auth

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fourteen06.emseesquare.repository.AuthWithPhoneUseCase
import com.fourteen06.emseesquare.repository.PhoneAuthResult
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authWithPhoneUseCase: AuthWithPhoneUseCase,
) : ViewModel() {
    fun init(authInStates: AuthInStates) {
        when (authInStates) {
            is AuthInStates.LoginWithPhoneNumber -> {
                viewModelScope.launch {
                    loginWithPhoneNumber(authInStates.phoneNumber, authInStates.activity)
                }
            }
        }
    }

    private val eventFlow = MutableStateFlow<AuthOutStates>(AuthOutStates.Uninitialized)
    val events: LiveData<AuthOutStates>
        get() = eventFlow.asLiveData()

    private suspend fun loginWithPhoneNumber(phoneNumber: String, activity: Activity) {
        val result = authWithPhoneUseCase(phoneNumber, activity)
        when (result) {
            is PhoneAuthResult.CodeSent -> {
                eventFlow.value =
                    AuthOutStates.MoveToOTP_Screen(result.verificationId, result.token)
            }
            is PhoneAuthResult.VerificationCompleted -> {
                eventFlow.value = AuthOutStates.Success
            }
        }
    }


}

sealed class AuthOutStates {
    object Uninitialized : AuthOutStates()
    object Loading : AuthOutStates()
    data class MoveToOTP_Screen(
        val verificationId: String,
        val token: PhoneAuthProvider.ForceResendingToken
    ) : AuthOutStates()

    object Success : AuthOutStates()
    data class Error(val message: String) : AuthOutStates()
}

sealed class AuthInStates {
    data class LoginWithPhoneNumber(val phoneNumber: String, val activity: Activity) :
        AuthInStates()

}