package com.fourteen06.emseesquare.presentation.chatscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.fourteen06.emseesquare.databinding.MessageLayout1Binding
import com.fourteen06.emseesquare.databinding.MessageLayout2Binding
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.models.MessageModel
import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.utils.UnixToHuman
import com.fourteen06.emseesquare.utils.load
import com.fourteen06.emseesquare.utils.loadProfileImage

class ChatAdapter(private val uid: String, private val userMap: Map<String, User>) :
    ListAdapter<MessageModel, ChatAdapter.AbstractViewHolder>(ChatAdapter.CustomDiffUtil) {
    abstract inner class AbstractViewHolder(val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(model: MessageModel)
    }

    inner class FirstViewHolder(binding: MessageLayout1Binding) : AbstractViewHolder(binding) {
        override fun bind(model: MessageModel) {
            (binding as MessageLayout1Binding).apply {
                chatMessage.setText(model.message)
                subtitleTextView.setText(UnixToHuman.getTimeAgo(model.time.time))
                userAvatarImageView.loadProfileImage(userMap[model.senderId]?.profileImageUrl)
                if (model.attachmentType == AttachmentType.None) {
                    imageView.visibility = View.GONE
                } else {
                    imageView.load(model.attachmentType.toURL())
                    imageView.visibility = View.VISIBLE
                }
            }
        }

    }

    inner class SecondViewHolder(binding: MessageLayout2Binding) : AbstractViewHolder(binding) {
        override fun bind(model: MessageModel) {
            (binding as MessageLayout2Binding).apply {
                chatMessage.setText(model.message)
                subtitleTextView.setText(UnixToHuman.getTimeAgo(model.time.time))
                userAvatarImageView.loadProfileImage(userMap[model.senderId]?.profileImageUrl)
                if (model.attachmentType == AttachmentType.None) {
                    imageView.visibility = View.GONE
                } else {
                    imageView.load(model.attachmentType.toURL())
                    imageView.visibility = View.VISIBLE
                }
            }
        }

    }

    object CustomDiffUtil : DiffUtil.ItemCallback<MessageModel>() {
        override fun areItemsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
            return oldItem.messageUid == newItem.messageUid
        }

        override fun areContentsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatAdapter.AbstractViewHolder {
        return when (viewType == 1) {
            true -> {
                val binding = MessageLayout2Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SecondViewHolder(binding)
            }

            false -> {
                val binding = MessageLayout1Binding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                FirstViewHolder(binding)
            }

        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = getItem(position)
        return when (currentItem.senderId != uid) {
            true -> 2
            false -> 1
        }
    }//        val binding =
//            MessageRecyclerViewLayoutBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        return ViewHolder(binding)

    override fun onBindViewHolder(holder: ChatAdapter.AbstractViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

}