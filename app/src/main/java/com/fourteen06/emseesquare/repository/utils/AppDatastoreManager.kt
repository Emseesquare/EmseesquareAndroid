//package com.fourteen06.emseesquare.repository.utils
//
//import android.app.Application
//import android.content.Context
//import androidx.datastore.core.DataStore
//import androidx.datastore.preferences.core.Preferences
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.intPreferencesKey
//import androidx.datastore.preferences.core.stringPreferencesKey
//import androidx.datastore.preferences.preferencesDataStore
//import com.fourteen06.emseesquare.models.User
//import com.fourteen06.emseesquare.models.UserRole
//import com.fourteen06.emseesquare.repository.utils.AppDatastoreManager.Companion.APP_SHARED_PREFERENCE
//import com.google.firebase.auth.FirebaseAuth
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//import javax.inject.Inject
//
//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = APP_SHARED_PREFERENCE)
//
//class AppDatastoreManager @Inject constructor(
//    app: Application,
//    private val firebaseAuth: FirebaseAuth
//) {
//
//    private val mDatastore: DataStore<Preferences> = app.dataStore
//
//    fun getCurrentStatus(): Flow<CurrentStatus> = mDatastore.data.map {
//        when (it[CURRENT_STATUS_KEY] ?: 0) {
//            0 -> CurrentStatus.LOGOUT
//            1 -> CurrentStatus.LOGGED_IN
//            else -> CurrentStatus.REGISTERED
//        }
//    }
//
//    suspend fun setCurrentStatus(currentStatus: CurrentStatus) {
//        mDatastore.edit {
//            it[CURRENT_STATUS_KEY] = when (currentStatus) {
//                CurrentStatus.LOGOUT -> 0
//                CurrentStatus.LOGGED_IN -> 1
//                CurrentStatus.REGISTERED -> 2
//            }
//        }
//    }
//
//    fun getCurrentUser(): Flow<User> = mDatastore.data.map {
//        User(
//            id = it[USER_ID_KEY] ?: "",
//            name = it[USER_NAME_KEY] ?: "",
//            profileImageUrl = it[USER_PROFILE_PICTURE_URL_KEY] ?: "",
//            role = when (it[USER_ROLE_KEY] ?: 0) {
//                0 -> UserRole.Student
//                1 -> UserRole.Teacher
//                2 -> UserRole.Admin
//                else -> throw  IllegalStateException(ILLEGAL_ROLE_EXCEPTION)
//            },
//            instituteId = it[USER_INSTITUTE_ID_KEY] ?: "",
//            subTitle = it[USER_SUBTITLE_KEY] ?: "",
//            uid = firebaseAuth.currentUser?.uid.toString()
//        )
//    }
//
//    suspend fun setUser(user: User) {
//        mDatastore.edit { preferences ->
//            preferences[USER_ID_KEY] = user.id
//            preferences[USER_NAME_KEY] = user.name
//            preferences[USER_PROFILE_PICTURE_URL_KEY] = user.profileImageUrl
//            preferences[USER_INSTITUTE_ID_KEY] = user.instituteId
//            preferences[USER_SUBTITLE_KEY] = user.subTitle
//            preferences[USER_ROLE_KEY] = when (user.role) {
//                UserRole.Student -> 0
//                UserRole.Teacher -> 1
//                UserRole.Admin -> 2
//            }
//
//        }
//    }
//
//    companion object {
//        const val APP_SHARED_PREFERENCE = "APP_SHARED_PREFERENCE"
//        private val CURRENT_STATUS_KEY = intPreferencesKey("CURRENT_STATUS_KEY")
//        private val USER_PROFILE_PICTURE_URL_KEY = stringPreferencesKey("USER_PROFILE_PICTURE_URL")
//        private val USER_ID_KEY = stringPreferencesKey("USER_ID_KEY")
//        private val USER_SUBTITLE_KEY = stringPreferencesKey("USER_SUBTITLE_KEY")
//        private val USER_INSTITUTE_ID_KEY = stringPreferencesKey("USER_INSTITUTE_ID_KEY")
//        private val USER_NAME_KEY = stringPreferencesKey("USER_NAME_KEY")
//        private val USER_ROLE_KEY = intPreferencesKey("USER_ROLE_KEY")
//        private const val ILLEGAL_ROLE_EXCEPTION =
//            "Illegal State: User can be Student, Teacher or Admin"
//
//    }
//
//    enum class CurrentStatus {
//        LOGOUT, LOGGED_IN, REGISTERED
//    }
//}