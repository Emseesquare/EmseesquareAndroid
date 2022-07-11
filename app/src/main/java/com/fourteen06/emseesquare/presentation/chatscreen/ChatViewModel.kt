package com.fourteen06.emseesquare.presentation.chatscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourteen06.emseesquare.models.MessageModel
import com.fourteen06.emseesquare.repository.message.AddMessageToChatUsecase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val addMessageToChatUsecase: AddMessageToChatUsecase,
    val firebaseAuth: FirebaseAuth
) : ViewModel() {
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
                    addMessageToChatUsecase(inState.roomId, messageModel)
                }
            }
        }
    }
}