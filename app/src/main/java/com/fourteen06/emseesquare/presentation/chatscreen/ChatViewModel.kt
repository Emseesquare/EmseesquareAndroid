package com.fourteen06.emseesquare.presentation.chatscreen

import android.net.Uri
import androidx.lifecycle.*
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.models.MessageModel
import com.fourteen06.emseesquare.repository.message.AddMessageToChatUsecase
import com.fourteen06.emseesquare.repository.message.GetChatUsecase
import com.fourteen06.emseesquare.repository.utils.FileUploadUseCase
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.StorageRoutes
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
    val getChatUsecase: GetChatUsecase,
    val fileUploadUseCase: FileUploadUseCase
) : ViewModel() {

    private val _latestTmpUri: MutableLiveData<Uri?> = MutableLiveData(null)
    val latestTmpUri: LiveData<Uri?>
        get() = _latestTmpUri

    //    var fileName: String? = null
    private val _fileName = MutableLiveData<String?>(null)
    val fileName: LiveData<String?>
        get() = _fileName
    val eventChannel = Channel<ChatViewModelOutState>()
    val events = eventChannel.receiveAsFlow().asLiveData()
    fun getCurrentChat(roomId: String) = getChatUsecase(roomId).asLiveData()

    fun init(inState: ChatViewModelInState) {
        when (inState) {
            is ChatViewModelInState.AddNewMessage -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (latestTmpUri.value == null) {
                        sendMessage(inState, AttachmentType.None)
                    } else {
                        fileUploadUseCase.invoke(
                            latestTmpUri.value!!,
                            storageLocation = StorageRoutes.GET_CHAT_IMAGE_STORAGE_URL(
                                inState.roomId,
                                fileName.value
                            ),
                            exclusiveFile = FileUploadUseCase.Companion.ExclusiveFile.IMAGE_ONLY
                        ).collect { response ->
                            when (response) {
                                is Resource.Error -> {
                                    eventChannel.send(ChatViewModelOutState.MakeToast(response.message))
                                }
                                is Resource.Loading -> {
                                    eventChannel.send(ChatViewModelOutState.ShowImageUploading)

                                }
                                is Resource.Success -> {
                                    sendMessage(inState, response.data)

                                }
                            }
                        }
                    }


                }
            }
            is ChatViewModelInState.SetImageUri -> {
                this._fileName.postValue(inState.fileName)
                this._latestTmpUri.postValue(inState.uri)

            }
            ChatViewModelInState.NullifyAttachment -> {
                this._fileName.postValue(null)
                this._latestTmpUri.postValue(null)
            }
        }
    }

    private suspend fun sendMessage(
        inState: ChatViewModelInState.AddNewMessage,
        attachmentType: AttachmentType
    ) {
        val messageModel = MessageModel(
            messageUid = UUID.randomUUID().toString(),
            message = inState.message,
            senderId = firebaseAuth.currentUser?.uid.toString(),
            time = Date(System.currentTimeMillis()),
            attachmentType = attachmentType
        )
        when (val response =
            addMessageToChatUsecase(inState.roomId, messageModel)) {
            is Resource.Error -> {
                eventChannel.send(
                    ChatViewModelOutState.MakeToast(
                        response.message
                    )
                )
            }
            is Resource.Loading -> {
                eventChannel.send(ChatViewModelOutState.ShowLoading)
            }
            is Resource.Success -> {
                init(ChatViewModelInState.NullifyAttachment)
                eventChannel.send(ChatViewModelOutState.MessageSendSuccessfully)
            }
        }
    }
}