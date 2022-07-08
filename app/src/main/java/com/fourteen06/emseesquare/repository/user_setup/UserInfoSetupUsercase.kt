package com.fourteen06.emseesquare.repository.user_setup

import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.models.User.Factory.toUser
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_url_locator.FirebaseFirestoreRoutes.getUserRoute
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
) {
    suspend fun checkForUserAvailability(uid: String): Boolean {
        val currentUserRef = firestore.collection(getUserRoute).document(uid).get().await()
        return currentUserRef.exists();
    }

    fun addNewUser(uid: String, user: User): Flow<Resource<User>> = flow {
        emit(Resource.Loading())
        try {
            val userRouteToDocument =
                firestore.collection(getUserRoute).document(uid)
            userRouteToDocument.set(user.toHashMap()).await()
            val userSnapshot = userRouteToDocument.get().await()
            val user = (userSnapshot).toUser()
            emit(Resource.Success(user))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(e.message.toString()))

        }
    }
}

