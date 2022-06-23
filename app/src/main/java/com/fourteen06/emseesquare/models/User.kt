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
            "id" to this.id,
            "name" to this.name,
            "subTitle" to this.subTitle,
            "profileImageUrl" to this.profileImageUrl,
            "role" to when (this.role) {
                UserRole.Admin -> UserRole.Admin
                UserRole.Student -> UserRole.Student
                is UserRole.Teacher -> this.role
            },
            "instituteId" to instituteId,
            "instituteName" to instituteName
        )
    }
}
sealed class UserRole {
    object Student : UserRole()
    data class Teacher(val teacherId: String) : UserRole()
    object Admin : UserRole()


}