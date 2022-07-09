package com.fourteen06.emseesquare.models

import com.google.firebase.firestore.DocumentSnapshot
import java.util.*

data class User(
    val uid: String,
    val id: String,
    val name: String,
    val subTitle: String,
    val profileImageUrl: String,
    val role: UserRole,
    val instituteId: String
) {
    fun toHashMap(): HashMap<String, *> {
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
        )
    }

    companion object Factory {
        fun DocumentSnapshot.toUser(): User {
            val dataMap = this.data
            return User(
                id = dataMap?.get(ID) as String,
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
                uid = dataMap[UID].toString()
            )
        }

    }

}

enum class UserRole {
    Student, Teacher, Admin
}

private const val UID = "uid"
private const val ID = "id"
private const val NAME = "name"
private const val SUBTITLE = "subtitle"
private const val PROFILE_IMAGE_URL = "profileImageUrl"
private const val ROLE = "role"
private const val INSTITUTE_ID = "instituteId"
