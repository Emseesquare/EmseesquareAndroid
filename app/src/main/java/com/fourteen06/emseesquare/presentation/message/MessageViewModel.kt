package com.fourteen06.emseesquare.presentation.message

import androidx.lifecycle.ViewModel
import com.fourteen06.emseesquare.repository.message.AddNewMessageRoomUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    val addNewMessageRoomUseCase: AddNewMessageRoomUseCase
) : ViewModel() {
    fun init(inStates: MessageViewmodelInStates) {
        when (inStates) {
            is MessageViewmodelInStates.MakeNewChatRoom -> {

            }
        }
    }
}