package com.fourteen06.emseesquare.presentation.community_posts

sealed class CommunityPostViewModelOutStates {
    object MessageSendSuccessfully : CommunityPostViewModelOutStates()
    data class ShowToast(val message: String) : CommunityPostViewModelOutStates()
}