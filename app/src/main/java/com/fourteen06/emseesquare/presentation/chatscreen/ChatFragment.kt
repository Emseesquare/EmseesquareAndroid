package com.fourteen06.emseesquare.presentation.chatscreen

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.models.MessageModel
import com.fourteen06.emseesquare.utils.MultistackBaseFragment
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ChatFragment : MultistackBaseFragment(
    R.layout.fragment_chat,
    title = "Sourav Kumar Sharma",
    isTopLevelFragment = false,
    titleRes = null,
    showHomeAsUp = true,
    homeIcon = null,
    hasMenu = false,
    menuRes = null
) {

    val list = listOf(
        MessageModel(
            messageUid = "1",
            message = "This is a Message",
            senderId = "1",
            time = Date(System.currentTimeMillis())
        ),
        MessageModel(
            messageUid = "2",
            message = "This is a Message",
            senderId = "1",
            time = Date(System.currentTimeMillis())
        ),
        MessageModel(
            messageUid = "1",
            message = "This is a Message",
            senderId = "2",
            time = Date(System.currentTimeMillis())
        ),
        MessageModel(
            messageUid = "1",
            message = "This is a Message",
            senderId = "1",
            time = Date(System.currentTimeMillis())
        ),
        MessageModel(
            messageUid = "2",
            message = "This is a Message",
            senderId = "1",
            time = Date(System.currentTimeMillis())
        ),
        MessageModel(
            messageUid = "2",
            message = "This is a Message",
            senderId = "2",
            time = Date(System.currentTimeMillis())
        ),


        )

    val chatAdapter = ChatAdapter("1")
    private val binding by viewBinding(com.fourteen06.emseesquare.databinding.FragmentChatBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(requireContext()).also {
                it.reverseLayout = false
            }
        }
        chatAdapter.submitList(this.list)
    }
}