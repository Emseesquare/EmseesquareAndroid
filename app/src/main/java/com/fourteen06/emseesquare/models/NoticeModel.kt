package com.fourteen06.emseesquare.models

data class NoticeModel(
    val id: String,
    val userName: String,
    val userId: String,
    val post: String,
    val time: Long,
    val message: String,
    val noOfImpressions: Long
)