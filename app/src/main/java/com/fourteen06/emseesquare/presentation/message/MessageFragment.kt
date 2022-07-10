package com.fourteen06.emseesquare.presentation.message

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.FragmentMessageBinding
import com.fourteen06.emseesquare.models.MessageRoom
import com.fourteen06.emseesquare.utils.MultistackBaseFragment
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MessageFragment : MultistackBaseFragment(
    R.layout.fragment_message,
    R.string.title_message,
    null,
    true,
    true,
    null,
    true,
    R.menu.generic_search_menu
) {
    private val binding by viewBinding(FragmentMessageBinding::bind)
    val adapter = MessageAdapter {
        findNavController().navigate(MessageFragmentDirections.actionMessageFragmentToChatFragment())
    }
    private val viewModel by viewModels<MessageViewModel>()
    val listOfMessage = listOf(
        MessageRoom(
            participant = listOf("aksjdfhadgasdg", "akjsdfhasdkjfhasd"),
            lastMessage = "Helloworld",
            lastMessageTimestamp = Date(System.currentTimeMillis()),
            roomOnline = true
        ),
        MessageRoom(
            participant = listOf("aksjdfhadgasdg", "akjsdfhasdkjfhasd"),
            lastMessage = "Helloworld",
            lastMessageTimestamp = Date(System.currentTimeMillis()),
            roomOnline = true
        ),
        MessageRoom(
            participant = listOf("aksjdfhadgasdg", "akjsdfhasdkjfhasd"),
            lastMessage = "Helloworld",
            lastMessageTimestamp = Date(System.currentTimeMillis()),
            roomOnline = true
        ),
        MessageRoom(
            participant = listOf("aksjdfhadgasdg", "akjsdfhasdkjfhasd"),
            lastMessage = "Helloworld",
            lastMessageTimestamp = Date(System.currentTimeMillis()),
            roomOnline = true
        ),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.messageRecyclerView.apply {
            adapter = this@MessageFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        adapter.submitList(listOfMessage)
    }

}