package com.fourteen06.emseesquare.presentation.chatscreen

import android.net.Uri

sealed class ChatViewModelInState {
    object NullifyAttachment : ChatViewModelInState()
    data class AddNewMessage(val message: String, val roomId: String) : ChatViewModelInState()
    data class SetImageUri(val uri: Uri?, val fileName: String?) : ChatViewModelInState()
}