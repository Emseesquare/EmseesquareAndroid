package com.fourteen06.emseesquare.repository.message

import com.fourteen06.emseesquare.models.MessageRoom
import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.repository.utils.AppSharedPreference
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute.MESSAGE_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddNewMessageRoomUseCase @Inject constructor(
    val firestore: FirebaseFirestore,
    val appSharedPreference: AppSharedPreference
) {
    suspend operator fun invoke(user: User): Flow<Resource<MessageRoom>> = flow {
        emit(Resource.Loading())
        try {
            val messageRoomId = UUID.randomUUID().toString()
            val messageRoom = MessageRoom(
                messageRoomId = messageRoomId,
                participant = mutableListOf<User>().also {
                    it.add(user)
                    it.add(appSharedPreference.getUser())
                },
                lastMessage = null,
                lastMessageTimestamp = Date(System.currentTimeMillis()),
                roomOnline = true
            )
            val messageRoomRef = firestore.collection(MESSAGE_COLLECTION).document(messageRoomId)
            messageRoomRef.set(messageRoom.toHashMap()).await()
            emit(Resource.Success(messageRoom))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}