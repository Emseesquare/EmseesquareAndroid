package com.fourteen06.emseesquare.repository.user_setup

import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.models.User.Factory.toUser
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserByName @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    operator fun invoke(name: String?): Flow<Resource<List<User>>> = flow {
        emit(Resource.Loading())
        try {

            val userSnapshot =
                firestore.collection(FirestoreRoute.PROFILE_COLLECTION)
                    .get().await().map {
                        it.toUser()
                    }.filter {
                        if (it.uid == firebaseAuth.currentUser?.uid.toString()) {
                            false
                        } else if (name != null) {
                            it.name.contains(name, ignoreCase = true)
                        } else true
                    }
            emit(Resource.Success(userSnapshot))

        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(e.message.toString()))
        }

    }
}