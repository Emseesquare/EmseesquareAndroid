package com.fourteen06.emseesquare.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*

data class MessageRoom(
    val messageRoomId: String,
    val participant: List<User>,
    val lastMessage: String?,
    val lastMessageTimestamp: Date,
    val roomOnline: Boolean
) {
    fun toHashMap(): HashMap<String, *> {
        return hashMapOf(
            MESSAGE_ROOM_ID to messageRoomId,
            PARTICIPANTS to mutableListOf<String>().also {
                for (i in participant) {
                    it.add(i.uid)
                }
            },
            LAST_MESSAGE to lastMessage,
            LAST_MESSAGE_TIMESTAMP to lastMessageTimestamp,
            ROOM_ONLINE to if (roomOnline) {
                1
            } else {
                0
            }
        )
    }

    companion object Factory {
        suspend fun QueryDocumentSnapshot.toMessageRoom(getUser: suspend (userId: String) -> User): MessageRoom {
            val dataMap = this.data
            val userIdList = dataMap[PARTICIPANTS] as List<*>
            val users = mutableListOf<User>().also {
                for (i in userIdList) {
                    it.add(getUser(i.toString()))
                }
            }
            return MessageRoom(
                messageRoomId = dataMap[MESSAGE_ROOM_ID].toString(),
                participant = users,
                lastMessageTimestamp = (dataMap[LAST_MESSAGE_TIMESTAMP] as Timestamp).toDate(),
                roomOnline = (dataMap[ROOM_ONLINE] as Int) == 1,
                lastMessage = dataMap[LAST_MESSAGE].toString()
            )
        }

        const val MESSAGE_ROOM_ID = "id"
        const val PARTICIPANTS = "participants"
        const val LAST_MESSAGE = "lastMessage"
        const val ROOM_ONLINE = "online"
        const val LAST_MESSAGE_TIMESTAMP = "lastMessageTimestamp"
    }

}