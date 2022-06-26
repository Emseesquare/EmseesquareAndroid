package com.fourteen06.emseesquare.presentation.notice

import androidx.lifecycle.ViewModel
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

}