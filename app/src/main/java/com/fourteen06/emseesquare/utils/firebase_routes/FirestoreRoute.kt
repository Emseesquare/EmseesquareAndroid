package com.fourteen06.emseesquare.utils.firebase_routes

object FirestoreRoute {
    const val PROFILE_COLLECTION = "profiles"
    const val NOTICES_PINNED_SUBCOLLECTION = "noticesPinned"
    fun GET_PROFILE_FROM_UID(uid: String) = "$PROFILE_COLLECTION/$uid"
    fun GET_NOTICES_PINNED_FROM_UID(uid: String) =
        "${GET_PROFILE_FROM_UID(uid)}/$NOTICES_PINNED_SUBCOLLECTION"

    /**/
    const val NOTICE_COLLECTION = "notices"
    /**/

    const val MESSAGE_COLLECTION = "messages"
    /**/

    const val COMMUNITY_COLLECTION = "communities"
    /**/


    fun GET_NOTICE_FROM_NOTICE_ID(noticeId: String) = "$NOTICE_COLLECTION/$noticeId"
    fun GET_MESSAGES_FROM_ROOM_ID(roomId: String) = "$MESSAGE_COLLECTION/$roomId/chat"
    fun GET_POSTS_FROM_COMMUNITY_ID(comunityId: String) = "$COMMUNITY_COLLECTION/$comunityId/posts"
    fun GET_POSTS_REACTORS_FROM_COMMUNITY_ID_AND_REACTION(
        comunityId: String,
        postId: String,
        reaction: Char
    ) = "$COMMUNITY_COLLECTION/$comunityId/posts/$postId/$reaction"
}