package com.fourteen06.emseesquare.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.QueryDocumentSnapshot
import java.util.*

data class CommunityPostModel(
    val communityPostId: String,
    val time: Date,
    val content: String,
    val attachmentType: AttachmentType,
    val user: User? = null,
    val reactions: List<Reaction>
) {
    fun toHashMap() = hashMapOf(
        COMMUNITY_POST_ID to communityPostId,
        TIME to time,
        CONTENT to content,
        ATTACHMENT_URL to attachmentType.toURL(),
        ATTACHMENT_TYPE to attachmentType.toType(),
        USER to user?.uid.toString(),
        REACTIONS to reactions.toHashMap()
    )

    companion object Factory {
        suspend fun QueryDocumentSnapshot.toCommunityPostModel(getUser: suspend (id: String) -> User?): CommunityPostModel {
            val dataMap = this.data
            val user = getUser(dataMap[USER].toString())
            val reaction = mutableListOf<Reaction>().also { reactionList ->
                (dataMap[REACTIONS] as Map<*, *>).forEach {
                    reactionList.add(
                        Reaction(
                            symbol = it.key.toString(),
                            it.value.toString().toInt()
                        )
                    )

                }
            }
            val attachmentType =
                when (dataMap[MessageModel.ATTACHMENT_TYPE] ?: AttachmentType.None) {
                    1L -> {
                        AttachmentType.Image(imageUrl = dataMap[MessageModel.ATTACHMENT_URL] as String)
                    }
                    2L -> {
                        AttachmentType.File(fileUrl = dataMap[MessageModel.ATTACHMENT_URL] as String)
                    }
                    else -> {
                        AttachmentType.None
                    }
                }
            return CommunityPostModel(
                communityPostId = dataMap[COMMUNITY_POST_ID].toString(),
                time = (dataMap[TIME] as Timestamp).toDate(),
                content = dataMap[CONTENT].toString(),
                user = user,
                reactions = reaction,
                attachmentType = attachmentType
            )
        }

        private const val COMMUNITY_POST_ID = "communityPostId"
        private const val TIME = "time"
        const val CONTENT = "content"
        const val ATTACHMENT_TYPE = "attachmentType"
        const val USER = "user"
        const val ATTACHMENT_URL = "attachmentUrl"
        const val REACTIONS = "reactions"
    }
}

data class Reaction(val symbol: String, val count: Int) {
    companion object {
        private const val SYMBOL = "symbol"
        private const val COUNT = "count"
    }
}

fun List<Reaction>.toHashMap() = mutableMapOf<String, Int>().also {
    this.forEach { reaction ->
        it[reaction.symbol] = reaction.count
    }
}
