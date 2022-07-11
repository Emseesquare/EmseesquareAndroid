package com.fourteen06.emseesquare.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*

data class MessageModel(
    val messageUid: String,
    val senderId: String,
    val message: String,
    val time: Date
) {
    fun toHashMap(): HashMap<String, *> {
        return hashMapOf(
            MESSAGE_UID to messageUid,
            SENDER_ID to senderId,
            MESSAGE to message,
            TIME to time
        )
    }

    companion object Factory {
        suspend fun QueryDocumentSnapshot.toMessage(): MessageModel {
            val dataMap = this.data
            return MessageModel(
                messageUid = dataMap[MESSAGE_UID].toString(),
                senderId = dataMap[SENDER_ID].toString(),
                message = dataMap[MESSAGE].toString(),
                time = (dataMap[TIME] as Timestamp).toDate(),
            )

        }

        const val MESSAGE_UID = "messageUid"
        const val SENDER_ID = "senderId"
        const val MESSAGE = "message"
        const val TIME = "time"
    }
}