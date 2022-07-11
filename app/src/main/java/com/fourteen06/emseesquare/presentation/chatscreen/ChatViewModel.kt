package com.fourteen06.emseesquare.presentation.chatscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fourteen06.emseesquare.models.MessageModel
import com.fourteen06.emseesquare.repository.message.AddMessageToChatUsecase
import com.fourteen06.emseesquare.repository.message.GetChatUsecase
import com.fourteen06.emseesquare.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val addMessageToChatUsecase: AddMessageToChatUsecase,
    val firebaseAuth: FirebaseAuth,
    val getChatUsecase: GetChatUsecase
) : ViewModel() {
    val eventChannel = Channel<ChatViewModelOutState>()
    val events = eventChannel.receiveAsFlow().asLiveData()
    fun getCurrentChat(roomId: String) = getChatUsecase(roomId).asLiveData()

    fun init(inState: ChatViewModelInState) {
        when (inState) {
            is ChatViewModelInState.AddNewMessage -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val messageModel = MessageModel(
                        messageUid = UUID.randomUUID().toString(),
                        message = inState.message,
                        senderId = firebaseAuth.currentUser?.uid.toString(),
                        time = Date(System.currentTimeMillis())
                    )
                    when (val response = addMessageToChatUsecase(inState.roomId, messageModel)) {
                        is Resource.Error -> {
                            eventChannel.send(ChatViewModelOutState.MakeToast(response.message))
                        }
                        is Resource.Loading -> {
                            eventChannel.send(ChatViewModelOutState.ShowLoading)
                        }
                        is Resource.Success -> {}
                    }
                }
            }
        }
    }
}