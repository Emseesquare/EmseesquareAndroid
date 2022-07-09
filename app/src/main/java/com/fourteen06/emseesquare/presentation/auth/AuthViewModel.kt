package com.fourteen06.emseesquare.presentation.auth

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fourteen06.emseesquare.di.CurrentUserUID
import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.models.UserRole
import com.fourteen06.emseesquare.repository.auth.AuthWithPhoneUseCase
import com.fourteen06.emseesquare.repository.auth.OtpAuthResult
import com.fourteen06.emseesquare.repository.auth.OtpAuthUseCase
import com.fourteen06.emseesquare.repository.auth.PhoneAuthResult
import com.fourteen06.emseesquare.repository.user_setup.UserInfoSetupUsercase
import com.fourteen06.emseesquare.utils.Resource
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authWithPhoneUseCase: AuthWithPhoneUseCase,
    private val otpAuthUseCase: OtpAuthUseCase,
    private val userInfoSetupUsercase: UserInfoSetupUsercase,
    @CurrentUserUID
    private val currentUserUid: String?,

    ) : ViewModel() {
    fun init(authInStates: AuthInStates) {
        when (authInStates) {
            is AuthInStates.LoginWithPhoneNumber -> {
                viewModelScope.launch(Dispatchers.IO) {
                    loginWithPhoneNumber(authInStates.phoneNumber, authInStates.activity)
                }
            }
            is AuthInStates.VerifyOTP -> {
                viewModelScope.launch(Dispatchers.IO) {
                    verifyOtp(authInStates.otp, authInStates.verificationId)
                }
            }
            is AuthInStates.SetUserInformation -> {
                viewModelScope.launch(Dispatchers.IO) {
                    createNewUser(authInStates)
                }
            }

        }
    }

    private suspend fun createNewUser(authInStates: AuthInStates.SetUserInformation) {
        val user = User(
            uid = currentUserUid.toString(),
            id = authInStates.id,
            name = authInStates.name,
            instituteId = authInStates.instituteId,
            subTitle = authInStates.subtitle,
            profileImageUrl = "",
            role = when (authInStates.role.lowercase(Locale.getDefault())) {
                "student" -> UserRole.Student
                "teacher" -> UserRole.Teacher
                "admin" -> UserRole.Admin
                else -> {
                    eventFlow.value =
                        AuthOutStates.Error("Illegal State: Profile can of student, teachers, admin only.")
                    return
                }
            }
        )
        userInfoSetupUsercase.addNewUser(currentUserUid.toString(), user).collect {
            when (it) {
                is Resource.Error -> {
                    eventFlow.value = AuthOutStates.Error(it.message)
                }
                is Resource.Loading -> {
                    eventFlow.value = AuthOutStates.Loading

                }
                is Resource.Success -> {
                    eventFlow.value = AuthOutStates.Success
                }
            }
        }
    }

    private val eventFlow = MutableStateFlow<AuthOutStates>(AuthOutStates.Uninitialized)
    val events: LiveData<AuthOutStates>
        get() = eventFlow.asLiveData()

    private suspend fun loginWithPhoneNumber(phoneNumber: String, activity: Activity) {
        eventFlow.value = AuthOutStates.Loading
        try {
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
        } catch (e: FirebaseException) {
            eventFlow.value = AuthOutStates.Error(e.message.toString())
        }
    }

    private suspend fun verifyOtp(otp: String, verificationId: String) {
        val result = otpAuthUseCase(otp, verificationId)
        when (result) {
            is OtpAuthResult.Error -> {
                eventFlow.value = AuthOutStates.Error(result.exception.message.toString())
            }
            is OtpAuthResult.Success -> {
                eventFlow.value = AuthOutStates.Success
            }
            OtpAuthResult.UnknownError -> {
                eventFlow.value = AuthOutStates.Error("Unknown Exception")
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

    data class VerifyOTP(
        val otp: String,
        val verificationId: String,
    ) : AuthInStates()

    data class SetUserInformation(
        val name: String,
        val id: String,
        val instituteId: String,
        val subtitle: String,
        val role: String,
    ) : AuthInStates()
}