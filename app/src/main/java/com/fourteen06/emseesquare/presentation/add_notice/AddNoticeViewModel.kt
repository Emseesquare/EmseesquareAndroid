package com.fourteen06.emseesquare.presentation.add_notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.models.NoticeModel
import com.fourteen06.emseesquare.repository.notice.AddNoticeUseCase
import com.fourteen06.emseesquare.repository.utils.AppSharedPreference
import com.fourteen06.emseesquare.repository.utils.FileUploadUseCase
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.StorageRoutes.NOTICE_COLLECTION_STORAGE
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddNoticeViewModel @Inject constructor(
    val fileUploadUseCase: FileUploadUseCase,
    val firebaseAuth: FirebaseAuth,
    val appSharedPreference: AppSharedPreference,
    val addNoticeUseCase: AddNoticeUseCase
) : ViewModel() {

    private val channel =
        Channel<AddNoticeViewModelOutStates>()
    val events = channel.receiveAsFlow()

    private val noticeUid = UUID.randomUUID().toString()
    private val attachmentState = MutableStateFlow<AttachmentType>(AttachmentType.None)
    val attachment: StateFlow<AttachmentType>
        get() = attachmentState

    fun init(
        viewModelInStates: AddNoticeViewModelInStates
    ) {
        when (viewModelInStates) {
            is AddNoticeViewModelInStates.UploadFileButtonClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    uploadFile(viewModelInStates)
                }
            }
            is AddNoticeViewModelInStates.SaveAttachment -> {
                this.attachmentState.value = viewModelInStates.attachmentType
            }
            is AddNoticeViewModelInStates.UploadNotice -> {
                viewModelScope.launch(Dispatchers.IO) {
                    saveNotice(viewModelInStates)
                }
            }
        }
    }

    private suspend fun saveNotice(viewModelInStates: AddNoticeViewModelInStates.UploadNotice) {
        val notice = NoticeModel(
            id = noticeUid,
            time = Date(System.currentTimeMillis()),
            content = viewModelInStates.description,
            pins = 0,
            attachmentType = this.attachment.value,
            user = appSharedPreference.getUser().also {
                it.uid = firebaseAuth.currentUser?.uid.toString()
            }
        )
        addNoticeUseCase(notice).collect {
            when (it) {
                is Resource.Error -> {
                    channel.send(AddNoticeViewModelOutStates.ErrorOccured(it.message))
                }
                is Resource.Loading -> {
                    channel.send(AddNoticeViewModelOutStates.NoticeAddingLoading)

                }
                is Resource.Success -> {
                    channel.send(AddNoticeViewModelOutStates.NoticeAddedSuccessful)

                }
            }
        }
    }

    private suspend fun uploadFile(viewModelInStates: AddNoticeViewModelInStates.UploadFileButtonClicked) {
        Logger.d(noticeUid)
        fileUploadUseCase(
            viewModelInStates.uri,
            "$NOTICE_COLLECTION_STORAGE/$noticeUid"
        ).collect {
            when (it) {
                is Resource.Error -> {
                    channel.send(AddNoticeViewModelOutStates.ErrorOccured(it.message))

                    Logger.e("ERROR", it.message)
                }
                is Resource.Loading -> {
                    channel.send(AddNoticeViewModelOutStates.AttachmentLoading)
                    Logger.i("LOADING")
                }
                is Resource.Success -> {
                    Logger.i("SUCCESSFUL")
                    channel.send(
                        AddNoticeViewModelOutStates.AttachmentLoadedSuccessful(
                            it.data
                        )
                    )
                }
            }
        }
    }

}