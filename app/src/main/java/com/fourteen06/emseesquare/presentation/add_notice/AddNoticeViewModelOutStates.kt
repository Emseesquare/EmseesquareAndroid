package com.fourteen06.emseesquare.presentation.add_notice

import com.fourteen06.emseesquare.models.AttachmentType

sealed class AddNoticeViewModelOutStates {
    data class ErrorOccured(val message: String) : AddNoticeViewModelOutStates()
    object AttachmentLoading : AddNoticeViewModelOutStates()
    data class AttachmentLoadedSuccessful(val attachmentType: AttachmentType) :
        AddNoticeViewModelOutStates()

    object NoticeAddingLoading : AddNoticeViewModelOutStates()
    object NoticeAddedSuccessful : AddNoticeViewModelOutStates()
}