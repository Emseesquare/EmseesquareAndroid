package com.fourteen06.emseesquare.presentation.add_notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.repository.utils.FileUploadUseCase
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_url_locator.FirebaseStorageUrlLocator.getNoticeUrlPrefix
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoticeViewModel @Inject constructor(
    val fileUploadUseCase: FileUploadUseCase
) : ViewModel() {

    private val channel =
        Channel<AddNoticeViewModelOutStates>()
    val events = channel.receiveAsFlow()

    private val attachmentState = MutableStateFlow<AttachmentType>(AttachmentType.None)
    val attachment: StateFlow<AttachmentType>
        get() = attachmentState

    fun init(
        viewModelInStates: AddNoticeViewModelInStates
    ) {
        when (viewModelInStates) {
            is AddNoticeViewModelInStates.UploadFileButtonClicked -> {
                viewModelScope.launch(Dispatchers.IO) {
                    fileUploadUseCase(
                        viewModelInStates.uri,
                        getNoticeUrlPrefix("34iu32y24387346")
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
            is AddNoticeViewModelInStates.SaveAttachment -> {
                this.attachmentState.value = viewModelInStates.attachmentType
            }
            is AddNoticeViewModelInStates.UploadNotice -> {

            }
        }
    }

}