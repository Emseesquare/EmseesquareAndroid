package com.fourteen06.emseesquare.presentation.community

import androidx.lifecycle.ViewModel
import com.fourteen06.emseesquare.models.CommunityModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(

) : ViewModel() {
    val list = mutableListOf(
        CommunityModel(
            communityId = "1",
            communityName = "First",
            communityImage = "https://images.pexels.com/photos/2188012/pexels-photo-2188012.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
            admin = listOf()
        ),
        CommunityModel(
            communityId = "1",
            communityName = "First",
            communityImage = "https://images.pexels.com/photos/2188012/pexels-photo-2188012.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
            admin = listOf()
        ),
        CommunityModel(
            communityId = "1",
            communityName = "First",
            communityImage = "https://images.pexels.com/photos/2188012/pexels-photo-2188012.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
            admin = listOf()
        ),
        CommunityModel(
            communityId = "1",
            communityName = "First",
            communityImage = "https://images.pexels.com/photos/2188012/pexels-photo-2188012.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
            admin = listOf()
        ),
    )

}