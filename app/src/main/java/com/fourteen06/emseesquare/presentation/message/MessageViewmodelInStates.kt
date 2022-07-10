package com.fourteen06.emseesquare.presentation.message

sealed class MessageViewmodelInStates {
    data class MakeNewChatRoom(val userId: String) : MessageViewmodelInStates()
}