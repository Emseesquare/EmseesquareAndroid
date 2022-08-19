package com.fourteen06.emseesquare.presentation.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fourteen06.emseesquare.repository.community.GetUserCommunitiesUseCase
import com.fourteen06.emseesquare.repository.community.SearchCommunityByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    val getUserCommunitiesUseCase: GetUserCommunitiesUseCase,
    val searchCommunityByNameUseCase: SearchCommunityByNameUseCase
) : ViewModel() {
    private val searchQuery = MutableStateFlow<String?>(null)
    private val flow = searchQuery.flatMapLatest {
        if (it == null) {
            getUserCommunitiesUseCase()
        } else {
            searchCommunityByNameUseCase(it)
        }
    }
    val communityLiveData = flow.asLiveData()
    val isEnrolledCommunities = searchQuery.value.isNullOrBlank()
    fun init(inState: CommunityViewModelInState) {
        when (inState) {
            is CommunityViewModelInState.SearchForCommunity -> {
                if (inState.searchQuery.isNullOrBlank()) {
                    searchQuery.value = null
                } else {
                    searchQuery.value = inState.searchQuery

                }
            }
            CommunityViewModelInState.SearchViewClosed -> {
                searchQuery.value = null
            }
            CommunityViewModelInState.SearchViewOpen -> {
            }
        }
    }
}