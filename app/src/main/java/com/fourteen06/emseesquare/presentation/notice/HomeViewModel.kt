package com.fourteen06.emseesquare.presentation.notice

//import com.fourteen06.emseesquare.repository.notice.getPagedNotices
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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
    private val pinNoticeUseCase: PinNoticeUseCase
) : ViewModel() {
    val notices = getAllNoticesUseCase().asLiveData()
    private val eventFlow = Channel<HomeOutState>()
    val events: Flow<HomeOutState>
        get() = eventFlow.receiveAsFlow()

    fun init(homeInState: HomeInState) {
        when (val inState = homeInState) {
            is HomeInState.ChangeLikeStatus -> {
                viewModelScope.launch(Dispatchers.IO) {
                    when (val response = pinNoticeUseCase(inState.noticeId)) {
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
        }

    }
}

sealed class HomeInState {
    data class ChangeLikeStatus(val noticeId: String) : HomeInState()
}

sealed class HomeOutState {
    data class MakeToast(val message: String) : HomeOutState()
}