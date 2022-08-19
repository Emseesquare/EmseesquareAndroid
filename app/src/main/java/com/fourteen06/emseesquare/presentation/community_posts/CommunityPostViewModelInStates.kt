package com.fourteen06.emseesquare.presentation.community_posts

import android.net.Uri
import com.fourteen06.emseesquare.models.CommunityModel

sealed class CommunityPostViewModelInStates {
    data class SendMessage(val message: String, val community: CommunityModel) :
        CommunityPostViewModelInStates()

    object NullifyAttachment : CommunityPostViewModelInStates()
    data class SetImageUri(val uri: Uri?, val fileName: String?) : CommunityPostViewModelInStates()
    data class SetAttachment(val uri: Uri?, val fileName: String?, val fileType: FileType) :
        CommunityPostViewModelInStates()

    object EnrolledCommunity : CommunityPostViewModelInStates()
    object UnenrolledCommunity : CommunityPostViewModelInStates()
    enum class FileType {
        IMAGE, PDF, OTHER
    }

}