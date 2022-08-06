package com.fourteen06.emseesquare.presentation.message

import com.fourteen06.emseesquare.models.User

sealed class MessageViewmodelInStates {
    data class FindUserByName(val name: String) : MessageViewmodelInStates()
    data class MakeNewChatRoom(val user: User) : MessageViewmodelInStates()
    object ShowMessageAdapter : MessageViewmodelInStates()
    object ShowSearchAdapter : MessageViewmodelInStates()
    object RestStateToUninstialized : MessageViewmodelInStates()
}