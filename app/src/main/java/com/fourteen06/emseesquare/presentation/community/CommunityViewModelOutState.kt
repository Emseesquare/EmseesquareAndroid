package com.fourteen06.emseesquare.presentation.community

sealed class CommunityViewModelOutState {
    data class MakeToast(val message: String, val isShort: Boolean = true) : CommunityViewModelOutState()
    object ShowLoading:CommunityViewModelOutState()
}
