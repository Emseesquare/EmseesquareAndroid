package com.fourteen06.emseesquare.repository.user_setup

import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.models.User.Factory.toUser
import com.fourteen06.emseesquare.repository.utils.AppSharedPreference
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute.PROFILE_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInfoSetupUsercase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val appSharedPreference: AppSharedPreference
) {

    suspend fun checkForUserLoginStatus(uid: String): AppSharedPreference.CurrentStatus {
        when (appSharedPreference.getUserStatus()) {
            AppSharedPreference.CurrentStatus.LOGOUT -> {
                return AppSharedPreference.CurrentStatus.LOGOUT
            }
            AppSharedPreference.CurrentStatus.LOGGED_IN -> {
                val currentUserPossibleDocument =
                    firestore.collection(PROFILE_COLLECTION).document(uid).get().await()
                if (currentUserPossibleDocument.exists()) {
                    appSharedPreference.setUserStatus(AppSharedPreference.CurrentStatus.REGISTERED)
                    appSharedPreference.setUser(currentUserPossibleDocument.toUser())
                    return AppSharedPreference.CurrentStatus.REGISTERED
                } else {
                    return AppSharedPreference.CurrentStatus.LOGGED_IN

                }
            }
            AppSharedPreference.CurrentStatus.REGISTERED -> {
                return AppSharedPreference.CurrentStatus.REGISTERED
            }
        }
    }

    fun addNewUser(uid: String, user: User): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val userRouteToDocument =
                firestore.collection(PROFILE_COLLECTION).document(uid)
            userRouteToDocument.set(user.toHashMap()).await()
            val userSnapshot = userRouteToDocument.get().await()
            val user = (userSnapshot).toUser()
            appSharedPreference.setUserStatus(AppSharedPreference.CurrentStatus.REGISTERED)
            appSharedPreference.setUser(user)
            emit(Resource.Success(user))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(e.message.toString()))

        }
    }

    suspend fun getUserById(uid: String): Resource<User> {
        return try {
            val userSnapshot =
                firestore.collection(PROFILE_COLLECTION).document(uid).get().await()
            Resource.Success(userSnapshot.toUser())
        } catch (e: FirebaseFirestoreException) {
            Resource.Error(e.message.toString())
        }
    }
}

