package com.fourteen06.emseesquare.presentation.notice

//import com.fourteen06.emseesquare.repository.notice.getPagedNotices
import androidx.lifecycle.*
import com.fourteen06.emseesquare.repository.notice.GetNoticeUseCase
import com.fourteen06.emseesquare.repository.notice.PinNoticeUseCase
import com.fourteen06.emseesquare.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllNoticesUseCase: GetNoticeUseCase,
    private val pinNoticeUseCase: PinNoticeUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val pinnedStatus = savedStateHandle.getLiveData(PINNED_STATUS, false)
    val notices = pinnedStatus.switchMap {
        getAllNoticesUseCase(it).asLiveData()
    }
    private val eventFlow = Channel<HomeOutState>()
    val events: Flow<HomeOutState>
        get() = eventFlow.receiveAsFlow()

    fun init(homeInState: HomeInState) {
        when (val inState = homeInState) {
            is HomeInState.ChangeLikeStatus -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (val response =
                        pinNoticeUseCase(inState.noticeId, inState.noticeTime, inState.isLiked)) {
                        is Resource.Error -> {
                            eventFlow.send(HomeOutState.MakeToast(response.message))
                        }
                        is Resource.Loading -> {
                            //Never Used
                        }
                        is Resource.Success -> {

                        }
                    }
                }
            }
            HomeInState.ShowAllNotices -> {
                savedStateHandle.set(PINNED_STATUS, false)
            }
            HomeInState.ShowPinnedNotices -> {
                savedStateHandle.set(PINNED_STATUS, true)

            }
        }

    }

    companion object {
        private const val PINNED_STATUS = "PINNED_STATUS"
    }
}

sealed class HomeInState {
    data class ChangeLikeStatus(val noticeId: String, val noticeTime: Long, val isLiked: Boolean) :
        HomeInState()

    object ShowPinnedNotices : HomeInState()
    object ShowAllNotices : HomeInState()
}

sealed class HomeOutState {
    data class MakeToast(val message: String) : HomeOutState()
}