package com.fourteen06.emseesquare.presentation.community_info

sealed class CommunityInfoViewModelOutState {
    data class MakeToast(val message: String, val isShort: Boolean = true) : CommunityInfoViewModelOutState()
    object ShowLoading:CommunityInfoViewModelOutState()
}
