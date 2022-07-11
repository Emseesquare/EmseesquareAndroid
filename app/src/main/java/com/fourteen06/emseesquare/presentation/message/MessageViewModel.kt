package com.fourteen06.emseesquare.presentation.message

import androidx.lifecycle.*
import com.fourteen06.emseesquare.repository.message.AddNewMessageRoomUseCase
import com.fourteen06.emseesquare.repository.message.GetAllCurrentMessageRoomUseCase
import com.fourteen06.emseesquare.repository.user_setup.GetUserByName
import com.google.firebase.auth.FirebaseAuth
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    val addNewMessageRoomUseCase: AddNewMessageRoomUseCase,
    val getAllCurrentMessageRoomUseCase: GetAllCurrentMessageRoomUseCase,
    val getUserByName: GetUserByName,
    val firebaseAuth: FirebaseAuth
) : ViewModel() {
    private val searchQuery = MutableStateFlow<String?>(null)
    val userFlow = searchQuery.flatMapLatest {
        getUserByName(it)
    }.asLiveData()

    val messageRoomFlow =
        getAllCurrentMessageRoomUseCase(firebaseAuth.currentUser?.uid.toString()).asLiveData()
    private val _adapterChangeIndicator =
        MutableLiveData(false) // false means normal message adapter should be used
    val adapterChangeIndicator: LiveData<Boolean>
        get() = _adapterChangeIndicator

    fun init(inStates: MessageViewmodelInStates) {
        when (inStates) {
            is MessageViewmodelInStates.MakeNewChatRoom -> {
                viewModelScope.launch(Dispatchers.IO) {
                    addNewMessageRoomUseCase(inStates.userId).collect {
                        Logger.i(it.toString())
                    }
                }
            }
            is MessageViewmodelInStates.FindUserByName -> {
                searchQuery.value = inStates.name
            }
            MessageViewmodelInStates.ShowMessageAdapter -> {
                _adapterChangeIndicator.value = false
            }
            MessageViewmodelInStates.ShowSearchAdapter -> {
                _adapterChangeIndicator.value = true

            }
        }
    }
}