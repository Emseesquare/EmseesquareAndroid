package com.fourteen06.emseesquare.presentation.community

sealed class CommunityViewModelInState {
    data class SearchForCommunity(val searchQuery: String) : CommunityViewModelInState()
    object SearchViewOpen : CommunityViewModelInState()
    object SearchViewClosed : CommunityViewModelInState()
}