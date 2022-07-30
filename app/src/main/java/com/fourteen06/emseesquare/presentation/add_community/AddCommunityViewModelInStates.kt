package com.fourteen06.emseesquare.presentation.add_community

import android.net.Uri
import com.fourteen06.emseesquare.models.AttachmentType

sealed class AddCommunityViewModelInStates {
    data class UploadFileButtonClicked(val uri: Uri) : AddCommunityViewModelInStates()
    data class SaveAttachment(val attachmentType: AttachmentType) :
        AddCommunityViewModelInStates()

    data class AddNewCommunity(val description: String) :
        AddCommunityViewModelInStates()
}