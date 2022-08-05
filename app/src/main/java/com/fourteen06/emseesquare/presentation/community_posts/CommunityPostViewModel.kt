package com.fourteen06.emseesquare.presentation.community_posts

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.models.CommunityModel
import com.fourteen06.emseesquare.repository.community.AddCommunityPostUseCase
import com.fourteen06.emseesquare.repository.community.GetCommunityPostUseCase
import com.fourteen06.emseesquare.repository.utils.FileUploadUseCase
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.StorageRoutes
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityPostViewModel @Inject constructor(
    private val addCommunityPostUseCase: AddCommunityPostUseCase,
    private val getCommunityPostUseCase: GetCommunityPostUseCase,
    private val fileUploadUseCase: FileUploadUseCase,
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext
    private val applicationContext: Context
) : ViewModel() {
    private val eventChannel = Channel<CommunityPostViewModelOutStates>()
    val events = eventChannel.receiveAsFlow()
    fun getCommunityPosts(communityModel: CommunityModel) =
        getCommunityPostUseCase(communityModel = communityModel).asLiveData()

    private val _fileUri: MutableLiveData<Uri?> = MutableLiveData(null)
    val attachmentUri: LiveData<Uri?>
        get() = _fileUri
    private val _fileName = MutableLiveData<String?>(null)
    val fileName: LiveData<String?>
        get() = _fileName
    private val _isImage = MutableLiveData<Boolean>(true)
    val isImage: LiveData<Boolean>
        get() = _isImage

    fun init(inStates: CommunityPostViewModelInStates) {
        when (inStates) {
            is CommunityPostViewModelInStates.SendMessage -> {
                viewModelScope.launch(Dispatchers.IO) {
                    if (attachmentUri.value == null) {
                        sendMessage(inStates, AttachmentType.None)
                    } else {
                        fileUploadUseCase.invoke(
                            attachmentUri.value!!,
                            storageLocation = StorageRoutes.GET_COMMUNITY_ATTACHMENT_STORAGE_URL(
                                inStates.community.communityId,
                                fileName.value
                            ),
                            exclusiveFile = FileUploadUseCase.Companion.ExclusiveFile.ALL
                        ).collect { response ->
                            when (response) {
                                is Resource.Error -> {
                                    eventChannel.send(
                                        CommunityPostViewModelOutStates.ShowToast(
                                            response.message
                                        )
                                    )
                                }
                                is Resource.Loading -> {
                                    eventChannel.send(CommunityPostViewModelOutStates.ShowImageUploading)

                                }
                                is Resource.Success -> {
                                    sendMessage(inStates, response.data)

                                }
                            }
                        }
                    }
                }
            }
            is CommunityPostViewModelInStates.SetImageUri -> {
                this._fileName.postValue(inStates.fileName)
                this._fileUri.postValue(inStates.uri)
            }
            CommunityPostViewModelInStates.NullifyAttachment -> {
                this._fileName.postValue(null)
                this._fileUri.postValue(null)
                _isImage.postValue(true)
            }
            is CommunityPostViewModelInStates.SetAttachment -> {
                when (inStates.fileType) {
                    CommunityPostViewModelInStates.FileType.IMAGE -> {
                        this._fileName.postValue(inStates.fileName)
                        this._fileUri.postValue(inStates.uri)
                        _isImage.postValue(true)
                    }
                    CommunityPostViewModelInStates.FileType.PDF -> {
                        _isImage.postValue(false)

                    }
                    CommunityPostViewModelInStates.FileType.OTHER -> {
                        viewModelScope.launch {
                            eventChannel.send(
                                CommunityPostViewModelOutStates.ShowToast(
                                    applicationContext.getString(
                                        R.string.invalid_file_type
                                    )
                                )
                            )
                        }
                        init(CommunityPostViewModelInStates.NullifyAttachment)
                    }
                }

            }
        }
    }

    private suspend fun sendMessage(
        inState: CommunityPostViewModelInStates.SendMessage,
        attachmentType: AttachmentType
    ) {
        when (val response =
            addCommunityPostUseCase(inState.community, inState.message, attachmentType)) {
            is Resource.Error -> {
                eventChannel.send(
                    CommunityPostViewModelOutStates.ShowToast(
                        response.message
                    )
                )
            }
            is Resource.Loading -> {
                eventChannel.send(CommunityPostViewModelOutStates.ShowLoading)
            }
            is Resource.Success -> {
                init(CommunityPostViewModelInStates.NullifyAttachment)
                eventChannel.send(CommunityPostViewModelOutStates.MessageSendSuccessfully)
            }
        }
    }
}