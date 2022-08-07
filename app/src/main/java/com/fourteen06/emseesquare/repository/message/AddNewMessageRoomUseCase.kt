package com.fourteen06.emseesquare.repository.message

import com.fourteen06.emseesquare.models.MessageRoom
import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.models.User.Factory.toMessageRefsId
import com.fourteen06.emseesquare.repository.user_setup.UserInfoSetupUsercase
import com.fourteen06.emseesquare.repository.utils.AppSharedPreference
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute.MESSAGE_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddNewMessageRoomUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val appSharedPreference: AppSharedPreference,
    private val firebaseAuth: FirebaseAuth,
    private val getMessageRoomById: GetMessageRoomById
) {
    suspend operator fun invoke(user:User): Flow<Resource<MessageRoom>> = flow {
        emit(Resource.Loading())
        try {
            val primaryUserRef = firestore.collection(FirestoreRoute.PROFILE_COLLECTION)
                .document(firebaseAuth.currentUser?.uid.toString())
            val primaryUser = primaryUserRef.get().await()
            val messageRoomMap = primaryUser.toMessageRefsId()
            if (messageRoomMap.keys.contains(user.uid)) {
                getCurrentChatRoom(this@AddNewMessageRoomUseCase, messageRoomMap, user, this)
            } else {
                val messageRoom = makeNewRoom(user, user.uid, primaryUser, primaryUserRef)
                emit(Resource.Success(messageRoom))
            }

        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }

    private suspend fun getCurrentChatRoom(
        addNewMessageRoomUseCase: AddNewMessageRoomUseCase,
        messageRoomMap: Map<String, String>,
        user: User,
        flowCollector: FlowCollector<Resource<MessageRoom>>
    ) {
        when (val response = addNewMessageRoomUseCase.getMessageRoomById(
            messageRoomId = messageRoomMap[user.uid]!!,
            user
        )) {
            is Resource.Error -> {
                flowCollector.emit(Resource.Error(response.message))
            }
            is Resource.Loading -> {
                //Never Used
            }
            is Resource.Success -> {
                flowCollector.emit(Resource.Success(response.data))
            }
        }
    }

    private suspend fun makeNewRoom(
        user: User,
        userId: String,
        primaryUser: DocumentSnapshot,
        primaryUserRef: DocumentReference
    ): MessageRoom {
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

        val secondaryUserRef =
            firestore.collection(FirestoreRoute.PROFILE_COLLECTION).document(userId)
        val secondaryUser = secondaryUserRef.get().await()
        val secondaryUserHashMap = secondaryUser.data.also {
            it?.set(User.MESSAGE_ROOM_REFS,
                secondaryUser.toMessageRefsId().toMutableMap().also { messageMap ->
                    messageMap[firebaseAuth.currentUser?.uid.toString()] = messageRoomId
                })
        }
        secondaryUserRef.set(secondaryUserHashMap!!).await()

        val primaryUserHashMap = primaryUser.data.also {
            it?.set(User.MESSAGE_ROOM_REFS,
                primaryUser.toMessageRefsId().toMutableMap().also { messageMap ->
                    messageMap[userId] = messageRoomId
                })
        }
        primaryUserRef.set(primaryUserHashMap!!).await()
        return messageRoom
    }
}