package com.fourteen06.emseesquare.models

import com.google.firebase.firestore.QueryDocumentSnapshot

data class CommunityModel(
    val communityId: String,
    val admin: List<String>,
    val communityName: String,
    val communityImage: String
) {
    fun toHashMap(): HashMap<String, *> {
        return hashMapOf(
            COMMUNITY_ID to communityId,
            COMMUNITY_NAME to communityName,
            COMMUNITY_IMAGE to communityImage,
            ADMIN to admin
        )
    }

    companion object {
        suspend fun QueryDocumentSnapshot.toCommunityModel(getUser: suspend (userId: String) -> User): CommunityModel {
            val dataMap = this.data
            val adminIdList = dataMap[ADMIN] as List<*>
            val admin = mutableListOf<String>().also {
                for (i in adminIdList) {
                    it.add(i.toString())
                }
            }
            return CommunityModel(
                communityId = dataMap[COMMUNITY_ID].toString(),
                admin = admin,
                communityImage = dataMap[COMMUNITY_IMAGE].toString(),
                communityName = dataMap[COMMUNITY_NAME].toString()
            )
        }

        const val COMMUNITY_ID = "messageRoomId"
        const val ADMIN = "admin"
        const val COMMUNITY_NAME = "communityName"

        const val COMMUNITY_IMAGE = "communityImage"

    }
}