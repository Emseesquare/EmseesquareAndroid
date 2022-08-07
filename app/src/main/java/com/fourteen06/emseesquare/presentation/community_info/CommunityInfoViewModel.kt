package com.fourteen06.emseesquare.presentation.community_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fourteen06.emseesquare.repository.community.GetCommunityUsers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class CommunityInfoViewModel @Inject constructor(
    private val getUsersByCommunityId: GetCommunityUsers
) : ViewModel() {
    private val searchQuery = MutableStateFlow<String?>(null)
    fun getUserFlow(communityId: String) = searchQuery.flatMapLatest {
        getUsersByCommunityId.invoke(communityId, it)
    }.asLiveData()

    fun init(inState: CommunityInfoViewModelInState) {
        when (inState) {
            is CommunityInfoViewModelInState.FindUserByName -> {
                this.searchQuery.value = inState.name

            }
            CommunityInfoViewModelInState.NullifySearchQuery -> {
                this.searchQuery.value = null
            }
        }
    }
}