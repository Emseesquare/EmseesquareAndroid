package com.fourteen06.emseesquare.presentation.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fourteen06.emseesquare.databinding.MessageRecyclerViewLayoutBinding
import com.fourteen06.emseesquare.models.MessageRoom
import com.fourteen06.emseesquare.utils.UnixToHuman.getTimeAgo
import com.fourteen06.emseesquare.utils.loadProfileImage

class MessageAdapter(val currentUserUid: String, private val onClick: (MessageRoom) -> Unit) :
    ListAdapter<MessageRoom, MessageAdapter.ViewHolder>(CustomDiffUtil()) {
    inner class ViewHolder(val binding: MessageRecyclerViewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(messageRoom: MessageRoom) {
            binding.apply {
                messageRoom.participant.filter {
                    it.uid != currentUserUid
                }[0].apply {
                    userNameTextView.text = name
                    profileImageView.loadProfileImage(profileImageUrl)
                }
                subtitleTextView.text = messageRoom.lastMessage
                timeTextView.text = getTimeAgo(messageRoom.lastMessageTimestamp.time)
                root.setOnClickListener {
                    onClick(messageRoom)
                }

            }
        }
    }


    class CustomDiffUtil : DiffUtil.ItemCallback<MessageRoom>() {
        override fun areItemsTheSame(
            oldItem: MessageRoom,
            newItem: MessageRoom
        ): Boolean {
            return oldItem.messageRoomId == newItem.messageRoomId
        }

        override fun areContentsTheSame(
            oldItem: MessageRoom,
            newItem: MessageRoom
        ): Boolean {
            return oldItem == newItem

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MessageRecyclerViewLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

}
