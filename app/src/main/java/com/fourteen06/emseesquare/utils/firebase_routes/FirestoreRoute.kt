package com.fourteen06.emseesquare.utils.firebase_routes

object FirestoreRoute {
    const val PROFILE_COLLECTION = "profiles"
    const val NOTICE_COLLECTION = "notices"
    const val MESSAGE_COLLECTION = "messages"
    const val COMMUNITY_COLLECTION = "communities"
    fun GET_MESSAGES_FROM_ROOM_ID(roomId: String) = "$MESSAGE_COLLECTION/$roomId/chat"
}