package com.fourteen06.emseesquare.models

data class User(
    val id: String,
    val name: String,
    val subTitle: String,
    val profileImageUrl: String,
    val role: UserRole,
    val instituteId: String,
    val instituteName: String
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
                is UserRole.Teacher -> this.role
            },
            INSTITUTE_ID to instituteId,
            INSTITUTE_NAME to instituteName
        )
    }

    companion object Factory {
//        fun QueryDocumentSnapshot.toUser(): User {
//            val dataMap = this.data
//            return User(
//                id=dataMap[ID] as String,
//                name= dataMap[NAME] as String,
//                subTitle = dataMap[SUBTITLE] as String,
//                profileImageUrl = dataMap[PROFILE_IMAGE_URL] as String,
//
//
//            )
//        }
    }
}

sealed class UserRole {
    object Student : UserRole()
    data class Teacher(val teacherId: String) : UserRole()
    object Admin : UserRole()
}

private const val ID = "id"
private const val NAME = "name"
private const val SUBTITLE = "subtitle"
private const val PROFILE_IMAGE_URL = "profileImageUrl"
private const val ROLE = "role"
private const val INSTITUTE_ID = "instituteId"
private const val INSTITUTE_NAME = "instituteName"