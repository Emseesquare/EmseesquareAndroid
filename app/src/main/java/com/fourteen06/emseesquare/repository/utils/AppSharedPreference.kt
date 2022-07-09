package com.fourteen06.emseesquare.repository.utils

import android.app.Application
import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.models.UserRole
import com.google.firebase.auth.FirebaseAuth
import hu.autsoft.krate.SimpleKrate
import hu.autsoft.krate.default.withDefault
import hu.autsoft.krate.intPref
import hu.autsoft.krate.stringPref
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSharedPreference @Inject constructor(
    application: Application,
    firebaseAuth: FirebaseAuth
) : SimpleKrate(application) {

    suspend fun setUser(user: User) {
        id = user.id
        name = user.name
        profilePhoto = user.profileImageUrl
        role = when (user.role) {
            UserRole.Student -> 0
            UserRole.Teacher -> 1
            UserRole.Admin -> 2
        }
        instituteId = user.instituteId
        subTitle = user.subTitle
        setUserStatus(CurrentStatus.REGISTERED)
    }

    suspend fun getUser(): User {
        return User(
            id = this.id.toString(),
            name = name.toString(),
            profileImageUrl = profilePhoto.toString(),
            role = when (role) {
                0 -> UserRole.Student
                1 -> UserRole.Teacher
                else -> UserRole.Admin
            },
            instituteId = instituteId.toString(),
            subTitle = subTitle.toString(),
        )
    }

    fun getUserStatus(): CurrentStatus {
        return when (currentUserStatus) {
            0 -> {
                CurrentStatus.LOGOUT
            }
            1 -> {
                CurrentStatus.LOGGED_IN
            }
            else -> {
                CurrentStatus.REGISTERED
            }
        }
    }

    fun setUserStatus(status: CurrentStatus) {
        currentUserStatus = when (status) {
            CurrentStatus.LOGOUT -> 0
            CurrentStatus.LOGGED_IN -> 1
            CurrentStatus.REGISTERED -> 2
        }
    }

    var id by stringPref(USER_ID_KEY)
    var name by stringPref(USER_NAME_KEY)
    var profilePhoto by stringPref(USER_PROFILE_PICTURE_URL_KEY)
    var role by intPref(USER_ROLE_KEY).withDefault(0)
    var instituteId by stringPref(USER_INSTITUTE_ID_KEY)
    var subTitle by stringPref(USER_SUBTITLE_KEY)
    var currentUserStatus by intPref(CURRENT_STATUS_KEY).withDefault(0)

    companion object {
        private const val CURRENT_STATUS_KEY = ("CURRENT_STATUS_KEY")
        private const val USER_PROFILE_PICTURE_URL_KEY = ("USER_PROFILE_PICTURE_URL")
        private const val USER_ID_KEY = ("USER_ID_KEY")
        private const val USER_SUBTITLE_KEY = ("USER_SUBTITLE_KEY")
        private const val USER_INSTITUTE_ID_KEY = ("USER_INSTITUTE_ID_KEY")
        private const val USER_NAME_KEY = ("USER_NAME_KEY")
        private const val USER_ROLE_KEY = ("USER_ROLE_KEY")
        private const val ILLEGAL_ROLE_EXCEPTION =
            "Illegal State: User can be Student, Teacher or Admin"
    }

    enum class CurrentStatus {
        LOGOUT, LOGGED_IN, REGISTERED
    }
}