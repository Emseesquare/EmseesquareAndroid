package com.fourteen06.emseesquare.presentation.chatscreen

sealed class ChatViewModelOutState {
    data class MakeToast(val message: String, val isShort: Boolean = true) : ChatViewModelOutState()
    object ShowLoading : ChatViewModelOutState()
    object MessageSendSuccessfully : ChatViewModelOutState() {

    }

    object ShowImageUploading : ChatViewModelOutState()
}
