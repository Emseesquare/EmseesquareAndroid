package com.fourteen06.emseesquare.models

import java.util.*

data class CommunityPostModel(
    val communityPostId: String,
    val time: Date,
    val content: String,
    val attachmentType: AttachmentType = AttachmentType.None,
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
//        suspend fun QueryDocumentSnapshot.toCommunityPostModel(getUser: (id: String) -> User?): CommunityPostModel {
//
//        }

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
