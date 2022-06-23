package com.fourteen06.emseesquare.presentation.notice

import androidx.lifecycle.ViewModel
import com.fourteen06.emseesquare.models.NoticeModel
import com.fourteen06.emseesquare.repository.getPagedNotices
import kotlinx.coroutines.flow.Flow

class HomeViewModel : ViewModel() {
    val notices: Flow<List<NoticeModel>>
        get() = getPagedNotices()
}