package com.fourteen06.emseesquare.models

import java.util.*

data class MessageRoom(
    val uid: String = UUID.randomUUID().toString(),
    val participant: List<String>,
    val lastMessage: String,
    val lastMessageTimestamp: Date,
    val roomOnline:Boolean
)