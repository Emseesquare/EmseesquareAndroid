package com.fourteen06.emseesquare.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*

data class NoticeModel(
    val id: String = "",
    val time: Date? = null,
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
            USER to user?.uid,
            ATTACHMENT_TYPE to attachmentType.toType()

        )
    }

    companion object Factory {
        suspend fun QueryDocumentSnapshot.toNotice(getUser: suspend (userId: String) -> User): NoticeModel {
            val dataMap = this.data
            val attachmentType = when (dataMap[ATTACHMENT_TYPE] as Int) {
                1 -> {
                    AttachmentType.Image(imageUrl = dataMap[ATTACHMENT_URL] as String)
                }
                2 -> {
                    AttachmentType.File(fileUrl = dataMap[ATTACHMENT_URL] as String)
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
    }
}

private const val ID = "id"
private const val TIME = "time"
private const val CONTENT = "content"
private const val PINS = "pins"
private const val ATTACHMENT_TYPE = "attachmentType"
private const val USER = "user"
private const val ATTACHMENT_URL = "attachment url"
