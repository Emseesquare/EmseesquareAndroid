package com.fourteen06.emseesquare.presentation.community_posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.repository.community.AddCommunityPostUseCase
import com.fourteen06.emseesquare.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunityPostViewModel @Inject constructor(
    private val addCommunityPostUseCase: AddCommunityPostUseCase
) : ViewModel() {
    private val eventChannel = Channel<CommunityPostViewModelOutStates>()
    val events = eventChannel.receiveAsFlow()
    fun init(inStates: CommunityPostViewModelInStates) {
        when (inStates) {
            is CommunityPostViewModelInStates.SendMessage -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (val response = addCommunityPostUseCase(
                        inStates.community,
                        message = inStates.message,
                        attachmentType = AttachmentType.None
                    )) {
                        is Resource.Error -> {
                            eventChannel.send(CommunityPostViewModelOutStates.ShowToast(response.message))
                        }
                        is Resource.Loading -> {
                            //Never Used
                        }
                        is Resource.Success -> {
                            eventChannel.send(CommunityPostViewModelOutStates.MessageSendSuccessfully)
                        }
                    }
                }
            }
        }
    }
}