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
import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.utils.AlertExt.makeLongToast
import com.fourteen06.emseesquare.utils.AlertExt.makeShortToast
import com.fourteen06.emseesquare.utils.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment(
    R.layout.fragment_chat,
) {
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
        val userMap = mutableMapOf<String, User>().also {
            for (i in args.messageRoom.participant) {
                it.put(i.uid, i)
            }
        }
        val chatAdapter = ChatAdapter(Firebase.auth.currentUser?.uid.toString(), userMap)
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            title = (args.messageRoom.participant.filter {
                it.uid != Firebase.auth.currentUser?.uid
            }[0].name)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding.included.cardView4.setOnClickListener {
            viewModel.init(
                ChatViewModelInState.AddNewMessage(
                    this.binding.included.chatEditText.text.toString(),
                    args.messageRoom.messageRoomId
                )
            )
            this.binding.included.chatEditText.setText("")
        }
        viewModel.getCurrentChat(args.messageRoom.messageRoomId).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    makeShortToast(it.message)
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    chatAdapter.submitList(it.data)
                    try{ binding.chatRecyclerView.smoothScrollToPosition(it.data.size - 1) }
                    catch (e:java.lang.IllegalArgumentException){}
                }
            }
        }
        viewModel.events.observe(viewLifecycleOwner) {
            when (it) {
                is ChatViewModelOutState.MakeToast -> {
                    if (it.isShort) {
                        makeShortToast(it.message)
                    } else {
                        makeLongToast(it.message)
                    }
                }
                ChatViewModelOutState.ShowLoading -> {

                }
            }
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