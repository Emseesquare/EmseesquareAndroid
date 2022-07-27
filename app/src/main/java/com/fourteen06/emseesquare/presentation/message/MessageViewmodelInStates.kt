package com.fourteen06.emseesquare.presentation.message

sealed class MessageViewmodelInStates {
    data class FindUserByName(val name: String) : MessageViewmodelInStates()
    data class MakeNewChatRoom(val userId: String) : MessageViewmodelInStates()
    object ShowMessageAdapter : MessageViewmodelInStates()
    object ShowSearchAdapter : MessageViewmodelInStates()
    object RestStateToUninstialized : MessageViewmodelInStates()
}