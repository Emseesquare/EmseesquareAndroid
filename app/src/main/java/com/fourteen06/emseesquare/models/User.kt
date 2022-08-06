package com.fourteen06.emseesquare.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class User(
    var uid: String,
    val id: String,
    val name: String,
    val subTitle: String,
    val profileImageUrl: String,
    val role: UserRole,
    val instituteId: String,
) : Parcelable {
    fun toHashMap(): MutableMap<String, Any> {
        return hashMapOf(
            ID to this.id,
            NAME to this.name,
            SUBTITLE to this.subTitle,
            PROFILE_IMAGE_URL to this.profileImageUrl,
            ROLE to when (this.role) {
                UserRole.Admin -> UserRole.Admin
                UserRole.Student -> UserRole.Student
                UserRole.Teacher -> UserRole.Teacher
            },
            INSTITUTE_ID to instituteId,
            UID to uid,

            )
    }

    companion object Factory {
        fun DocumentSnapshot.toUser(): User {
            val dataMap = this.data
            if (dataMap != null) {
                return User(
                    id = dataMap[ID] as String,
                    name = dataMap[NAME] as String,
                    subTitle = dataMap[SUBTITLE] as String,
                    profileImageUrl = dataMap[PROFILE_IMAGE_URL] as String,
                    role = if (dataMap[ROLE].toString().lowercase(Locale.getDefault())
                            .contains("admin")
                    ) {
                        UserRole.Admin
                    } else if (dataMap[ROLE].toString().lowercase(Locale.getDefault())
                            .contains("teacher")
                    ) {
                        UserRole.Teacher
                    } else {
                        UserRole.Student
                    },
                    instituteId = dataMap[INSTITUTE_ID] as String,
                    uid = dataMap[UID].toString(),
                )
            } else {
                throw IllegalStateException("Datamap is null")
            }
        }

        fun DocumentSnapshot.toUserCommunities(): List<String> {
            val dataMap = this.data
            if (dataMap != null) {
                val adminIdList = dataMap[COMMUNITY_REFS] as List<*>?
                if (adminIdList.isNullOrEmpty()) return emptyList()
                return mutableListOf<String>().also {
                    for (i in adminIdList) {
                        it.add(i.toString())
                    }
                }

            } else {
                throw IllegalStateException("Datamap is null")
            }
        }

        fun DocumentSnapshot.toMessageRefsId(): Map<String, String> {
            val dataMap = this.data
            if (dataMap != null) {
                val messageContactMap = dataMap[MESSAGE_ROOM_REFS] as Map<*, *>?
                if (messageContactMap.isNullOrEmpty()) return emptyMap()
                return messageContactMap.map { (key, value) ->
                    key.toString() to value.toString()
                }.toMap()

            } else {
                throw IllegalStateException("Datamap is null")
            }
        }


        const val UID = "uid"
        const val ID = "id"
        const val NAME = "name"
        const val SUBTITLE = "subtitle"
        const val PROFILE_IMAGE_URL = "profileImageUrl"
        const val ROLE = "role"
        const val INSTITUTE_ID = "instituteId"
        const val COMMUNITY_REFS = "communityRefs"
        const val MESSAGE_ROOM_REFS = "messageRoomsRefs"
    }
}

enum class UserRole {
    Student, Teacher, Admin
}
