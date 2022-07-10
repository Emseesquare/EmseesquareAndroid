package com.fourteen06.emseesquare.presentation.message

import com.fourteen06.emseesquare.models.User

sealed class MessageViewmodelInStates {
    data class MakeNewChatRoom(val user: User) : MessageViewmodelInStates()
}