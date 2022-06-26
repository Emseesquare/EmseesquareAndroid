package com.fourteen06.emseesquare.presentation.notice

import androidx.lifecycle.ViewModel
import com.fourteen06.emseesquare.models.NoticeModel
import com.fourteen06.emseesquare.repository.getPagedNotices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class HomeViewModel : ViewModel() {
    val lastVisibleItem = MutableStateFlow<Int>(0)
    val notices: Flow<List<NoticeModel>>
        get() = getPagedNotices(lastVisibleItem)

}