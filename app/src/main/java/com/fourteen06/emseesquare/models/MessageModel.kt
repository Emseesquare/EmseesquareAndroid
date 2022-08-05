package com.fourteen06.emseesquare.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*

data class MessageModel(
    val messageUid: String,
    val senderId: String,
    val message: String,
    val time: Date,
    val attachmentType: AttachmentType
) {
    fun toHashMap(): HashMap<String, *> {
        return hashMapOf(
            MESSAGE_UID to messageUid,
            SENDER_ID to senderId,
            MESSAGE to message,
            TIME to time,
            ATTACHMENT_URL to attachmentType.toURL(),
            ATTACHMENT_TYPE to attachmentType.toType(),
            THUMBNAIL_URL to attachmentType.toThumbnailURL(),

            )
    }

    companion object Factory {
        suspend fun QueryDocumentSnapshot.toMessage(): MessageModel {
            val dataMap = this.data
            val attachmentType = when (dataMap[ATTACHMENT_TYPE] ?: AttachmentType.None) {
                1L -> {
                    AttachmentType.Image(imageUrl = dataMap[ATTACHMENT_URL] as String)
                }
                2L -> {
                    AttachmentType.File(
                        fileUrl = dataMap[ATTACHMENT_URL] as String,
                        thumbnail = dataMap[THUMBNAIL_URL] as String
                    )
                }
                else -> {
                    AttachmentType.None
                }
            }
            return MessageModel(
                messageUid = dataMap[MESSAGE_UID].toString(),
                senderId = dataMap[SENDER_ID].toString(),
                message = dataMap[MESSAGE].toString(),
                time = (dataMap[TIME] as Timestamp).toDate(),
                attachmentType = attachmentType
            )

        }

        const val MESSAGE_UID = "messageUid"
        const val SENDER_ID = "senderId"
        const val MESSAGE = "message"
        const val TIME = "time"
        const val ATTACHMENT_URL = "attachmentUrl"
        const val ATTACHMENT_TYPE = "attachmentType"
        const val THUMBNAIL_URL = "thumbnailUrl"

    }
}