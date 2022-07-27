package com.fourteen06.emseesquare.presentation.message

import com.fourteen06.emseesquare.models.MessageRoom

sealed class MessageViewmodelOutStates {
    data class MakeToast(val message: String) : MessageViewmodelOutStates()
    object Uninitialized : MessageViewmodelOutStates()
    data class MoveToChatRoom(val messageRoom: MessageRoom) : MessageViewmodelOutStates()

}