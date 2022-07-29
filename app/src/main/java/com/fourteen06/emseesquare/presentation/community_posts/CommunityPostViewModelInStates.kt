package com.fourteen06.emseesquare.presentation.community_posts

import com.fourteen06.emseesquare.models.CommunityModel

sealed class CommunityPostViewModelInStates {
    data class SendMessage(val message: String, val community: CommunityModel) :
        CommunityPostViewModelInStates()
}