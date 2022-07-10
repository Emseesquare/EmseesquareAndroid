package com.fourteen06.emseesquare.presentation.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourteen06.emseesquare.repository.message.AddNewMessageRoomUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    val addNewMessageRoomUseCase: AddNewMessageRoomUseCase
) : ViewModel() {
    fun init(inStates: MessageViewmodelInStates) {
        when (inStates) {
            is MessageViewmodelInStates.MakeNewChatRoom -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addNewMessageRoomUseCase(inStates.userId).collect {
                        Logger.d(it)
                    }
                }
            }
        }
    }
}