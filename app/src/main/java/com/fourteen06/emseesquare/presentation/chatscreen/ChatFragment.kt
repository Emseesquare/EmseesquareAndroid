package com.fourteen06.emseesquare.presentation.chatscreen

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.models.MessageModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ChatFragment : Fragment(
    R.layout.fragment_chat,
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
    private val args by navArgs<ChatFragmentArgs>()
    private val binding by viewBinding(com.fourteen06.emseesquare.databinding.FragmentChatBinding::bind)
    private val viewModel by viewModels<ChatViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            this.binding.included.chatEditText.setText(
                savedInstanceState.getString(
                    MESSAGE_EDIT_TEXT_TAG
                )
            )
        }
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        }
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            title = (args.messageRoom.participant.filter {
                it.uid != Firebase.auth.currentUser?.uid
            }[0].name)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        chatAdapter.submitList(this.list)
        binding.included.cardView4.setOnClickListener {
            viewModel.init(
                ChatViewModelInState.AddNewMessage(
                    this.binding.included.chatEditText.text.toString(),
                    args.messageRoom.messageRoomId
                )
            )
        }
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            //act as backbutton
            findNavController().popBackStack()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(
            MESSAGE_EDIT_TEXT_TAG,
            this.binding.included.chatEditText.text.toString()
        )
    }

    companion object {
        private val MESSAGE_EDIT_TEXT_TAG = "MESSAGE_EDIT_TEXT_TAG"
    }
}