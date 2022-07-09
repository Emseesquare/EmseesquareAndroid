package com.fourteen06.emseesquare.presentation.add_notice

import android.net.Uri
import com.fourteen06.emseesquare.models.AttachmentType

sealed class AddNoticeViewModelInStates {
    data class UploadFileButtonClicked(val uri: Uri) : AddNoticeViewModelInStates()
    data class SaveAttachment(val attachmentType: AttachmentType) :
        AddNoticeViewModelInStates()

    data class UploadNotice(val description: String) :
        AddNoticeViewModelInStates()
}