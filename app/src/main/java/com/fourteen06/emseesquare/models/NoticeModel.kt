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
            ATTACHMENT_TYPE to this.attachmentType,
            USER to user
        )
    }

    companion object Factory {
        fun QueryDocumentSnapshot.toNotice(): NoticeModel {
            val dataMap = this.data
            return NoticeModel(
                id = dataMap[ID] as String,
                content = dataMap[CONTENT].toString(),
                time = (dataMap[TIME] as Timestamp).toDate(),
                attachmentType = when (dataMap[ATTACHMENT_TYPE]) {
                    1L -> {
                        AttachmentType.Image(imageUrl = dataMap[IMAGE_URL] as String)
                    }
                    2L -> {
                        AttachmentType.File(fileUrl = dataMap[FILE_URL] as String)
                    }
                    else -> {
                        AttachmentType.None
                    }
                }
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
