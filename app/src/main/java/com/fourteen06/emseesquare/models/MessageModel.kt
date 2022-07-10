package com.fourteen06.emseesquare.models

import java.util.*

data class MessageModel(
    val messageUid:String,
    val senderId:String,
    val message:String,
    val time:Date
) {
}