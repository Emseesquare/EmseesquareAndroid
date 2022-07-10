package com.fourteen06.emseesquare.presentation.notice

//import com.fourteen06.emseesquare.repository.notice.getPagedNotices
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fourteen06.emseesquare.repository.notice.GetNoticeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllNoticesUseCase: GetNoticeUseCase
) : ViewModel() {
    val lastVisibleItem = MutableStateFlow<Int>(0)
    val notices = getAllNoticesUseCase().asLiveData()
    private val eventFlow = MutableStateFlow<HomeOutState>(HomeOutState.Uninitialized)
    val events: LiveData<HomeOutState>
        get() = eventFlow.asLiveData()

    fun init(homeInState: HomeInState) {
        when (homeInState) {
            HomeInState.AddNewNotice -> {
                eventFlow.value = HomeOutState.AddNewNotice
            }
        }

    }
}

sealed class HomeInState {
    object AddNewNotice : HomeInState()
}

sealed class HomeOutState {
    object Uninitialized : HomeOutState()
    object AddNewNotice : HomeOutState()
}