package com.fourteen06.emseesquare.repository.message

import com.fourteen06.emseesquare.models.MessageModel
import com.fourteen06.emseesquare.models.MessageRoom
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddMessageToChatUsecase @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend operator fun invoke(roomId: String, message: MessageModel): Resource<MessageModel> {
        return try {
            val ref = db.collection(FirestoreRoute.GET_MESSAGES_FROM_ROOM_ID(roomId))
                .document(message.messageUid)
            ref.set(message.toHashMap())
                .await()
            val roomRef = db.collection(FirestoreRoute.MESSAGE_COLLECTION).document(roomId)
            roomRef.update(
                mapOf(
                    MessageRoom.LAST_MESSAGE to message.message,
                    MessageRoom.LAST_MESSAGE_TIMESTAMP to message.time
                )
            ).await()
            Resource.Success(message)
        } catch (e: FirebaseFirestoreException) {
            Resource.Error(message = e.message.toString())
        }
    }
}