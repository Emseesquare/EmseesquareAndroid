package com.fourteen06.emseesquare.repository.message

import com.fourteen06.emseesquare.models.MessageRoom
import com.fourteen06.emseesquare.models.MessageRoom.Factory.toMessageRoom
import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.repository.user_setup.UserInfoSetupUsercase
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMessageRoomById @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userInfoSetupUsercase: UserInfoSetupUsercase
) {
    suspend operator fun invoke(messageRoomId: String, user: User? = null): Resource<MessageRoom> {
        val messageRoom =
            firestore.collection(FirestoreRoute.MESSAGE_COLLECTION).document(messageRoomId).get()
                .await()
        if (user != null) {
            return Resource.Success((messageRoom).toMessageRoom {
                user
            })
        } else
            return Resource.Success((messageRoom as QueryDocumentSnapshot).toMessageRoom {
                when (val response = userInfoSetupUsercase.getUserById(it)) {
                    is Resource.Error -> {
                        throw IllegalStateException(response.message)
                    }
                    is Resource.Loading -> {
                        throw IllegalStateException("This usecase should never rise.")

                    }
                    is Resource.Success -> {
                        response.data
                    }
                }
            })
    }

}