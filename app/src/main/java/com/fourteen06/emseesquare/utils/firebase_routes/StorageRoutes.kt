package com.fourteen06.emseesquare.utils.firebase_routes

object StorageRoutes {
    const val PROFILE_COLLECTION_STORAGE = "profiles"
    const val NOTICE_COLLECTION_STORAGE = "notices"
    const val MESSAGE_COLLECTION_STORAGE = "messages"
    const val COMMUNITY_COLLECTION_STORAGE = "communities"

    fun getProfilePhotoStorageUrl(uid: String?) =
        "$PROFILE_COLLECTION_STORAGE/${uid.toString()}"

    fun GET_CHAT_IMAGE_STORAGE_URL(roomId: String, fileName: String?) =
        MESSAGE_COLLECTION_STORAGE + "/${roomId}/${fileName}"

    fun GET_COMMUNITY_ATTACHMENT_STORAGE_URL(communityId: String, fileName: String?) =
        COMMUNITY_COLLECTION_STORAGE + "/${communityId}/${fileName}"
}