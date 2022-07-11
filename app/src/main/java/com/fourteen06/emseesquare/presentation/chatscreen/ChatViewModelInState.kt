package com.fourteen06.emseesquare.presentation.chatscreen

sealed class ChatViewModelInState {
    data class AddNewMessage(val message: String, val roomId:String) : ChatViewModelInState()
}