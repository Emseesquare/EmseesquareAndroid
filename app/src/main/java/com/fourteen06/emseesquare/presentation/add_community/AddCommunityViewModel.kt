package com.fourteen06.emseesquare.presentation.add_community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.models.CommunityModel
import com.fourteen06.emseesquare.repository.community.AddNewCommunityRoomUseCase
import com.fourteen06.emseesquare.repository.utils.FileUploadUseCase
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.StorageRoutes
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddCommunityViewModel @Inject constructor(
    val fileUploadUseCase: FileUploadUseCase,
    val addNewCommunityRoomUseCase: AddNewCommunityRoomUseCase
) : ViewModel() {
    private val channel =
        Channel<AddCommunityViewModelOutStates>()
    val events = channel.receiveAsFlow()
    private val communityUid = UUID.randomUUID().toString()
    private val attachmentState = MutableStateFlow<AttachmentType>(AttachmentType.None)
    val attachment: StateFlow<AttachmentType>
        get() = attachmentState

    fun init(inStates: AddCommunityViewModelInStates) {
        when (inStates) {
            is AddCommunityViewModelInStates.AddNewCommunity -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addNewCommunity(inStates.description)
                }
            }
            is AddCommunityViewModelInStates.SaveAttachment -> {
                this.attachmentState.value = inStates.attachmentType

            }
            is AddCommunityViewModelInStates.UploadFileButtonClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    uploadFile(inStates)
                }
            }
        }
    }

    private suspend fun addNewCommunity(communityName: String) {
        val communityModel = CommunityModel(
            communityId = this.communityUid,
            communityImage = if (attachment.value is AttachmentType.Image) {
                (attachment.value as AttachmentType.Image).imageUrl
            } else "",
            admin = listOf(
                Firebase.auth.currentUser?.uid.toString()
            ),
            communityName = communityName
        )
        addNewCommunityRoomUseCase(this.communityUid, communityModel).collect {
            when (it) {
                is Resource.Error -> {
                    channel.send(AddCommunityViewModelOutStates.ErrorOccured(it.message))
                }
                is Resource.Loading -> {
                    channel.send(AddCommunityViewModelOutStates.CommunityAddingLoading)

                }
                is Resource.Success -> {
                    channel.send(AddCommunityViewModelOutStates.CommunityAddedSuccessful)

                }
            }
        }
    }

    private suspend fun uploadFile(viewModelInStates: AddCommunityViewModelInStates.UploadFileButtonClicked) {
        fileUploadUseCase(
            viewModelInStates.uri,
            "${StorageRoutes.COMMUNITY_COLLECTION_STORAGE}/$communityUid",
            exclusiveFile = FileUploadUseCase.Companion.ExclusiveFile.IMAGE_ONLY
        ).collect {
            when (it) {
                is Resource.Error -> {
                    channel.send(AddCommunityViewModelOutStates.ErrorOccured(it.message))
                    Logger.e("ERROR", it.message)
                }
                is Resource.Loading -> {
                    channel.send(AddCommunityViewModelOutStates.AttachmentLoading)
                    Logger.i("LOADING")
                }
                is Resource.Success -> {
                    Logger.i("SUCCESSFUL")
                    channel.send(
                        AddCommunityViewModelOutStates.AttachmentLoadedSuccessful(
                            it.data
                        )
                    )
                }
            }
        }
    }
}