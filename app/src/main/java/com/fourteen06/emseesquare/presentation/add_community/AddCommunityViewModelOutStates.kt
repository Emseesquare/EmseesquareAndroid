package com.fourteen06.emseesquare.presentation.add_community

import com.fourteen06.emseesquare.models.AttachmentType

sealed class AddCommunityViewModelOutStates {
    data class ErrorOccured(val message: String) : AddCommunityViewModelOutStates()
    object AttachmentLoading : AddCommunityViewModelOutStates()
    data class AttachmentLoadedSuccessful(val attachmentType: AttachmentType) :
        AddCommunityViewModelOutStates()

    object CommunityAddingLoading : AddCommunityViewModelOutStates()
    object CommunityAddedSuccessful : AddCommunityViewModelOutStates()
}