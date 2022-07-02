package com.fourteen06.emseesquare.presentation.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fourteen06.emseesquare.models.NoticeModel
import com.fourteen06.emseesquare.repository.getPagedNotices
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    val lastVisibleItem = MutableStateFlow<Int>(0)
    val notices: Flow<List<NoticeModel>>
        get() = getPagedNotices(lastVisibleItem)
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