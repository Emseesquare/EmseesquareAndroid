package com.fourteen06.emseesquare.presentation.community_info

sealed class CommunityInfoViewModelInState {
    data class FindUserByName(val name: String) : CommunityInfoViewModelInState()
    object NullifySearchQuery : CommunityInfoViewModelInState()
}
