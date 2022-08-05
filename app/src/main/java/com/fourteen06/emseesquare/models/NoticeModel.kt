package com.fourteen06.emseesquare.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*

data class NoticeModel(
    val id: String = "",
    val time: Date,
    val content: String = "",
    val pins: Int = 0,
    val attachmentType: AttachmentType = AttachmentType.None,
    val user: User? = null
) {
    fun toHashMap(): HashMap<String, *> {
        return hashMapOf(
            ID to this.id,
            TIME to this.time,
            CONTENT to this.content,
            PINS to this.pins,
            ATTACHMENT_URL to attachmentType.toURL(),
            THUMBNAIL_URL to attachmentType.toThumbnailURL(),
            USER to user?.uid,
            ATTACHMENT_TYPE to attachmentType.toType()

        )
    }

    companion object Factory {
        suspend fun QueryDocumentSnapshot.toNotice(getUser: suspend (userId: String) -> User): NoticeModel {
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
            val user = getUser(dataMap[USER].toString())
            return NoticeModel(
                id = dataMap[ID] as String,
                content = dataMap[CONTENT].toString(),
                time = (dataMap[TIME] as Timestamp).toDate(),
                attachmentType = attachmentType,
                user = user
            )
        }

        const val ID = "id"
        const val TIME = "time"
        const val CONTENT = "content"
        const val PINS = "pins"
        const val ATTACHMENT_TYPE = "attachmentType"
        const val USER = "user"
        const val ATTACHMENT_URL = "attachmentUrl"
        const val THUMBNAIL_URL = "thumbnailUrl"
    }
}
