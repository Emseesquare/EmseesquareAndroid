package com.fourteen06.emseesquare.presentation.chatscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.fourteen06.emseesquare.databinding.MessageLayout1Binding
import com.fourteen06.emseesquare.databinding.MessageLayout2Binding
import com.fourteen06.emseesquare.models.MessageModel

class ChatAdapter(private val uid: String) :
    ListAdapter<MessageModel, ChatAdapter.AbstractViewHolder>(ChatAdapter.CustomDiffUtil) {
    abstract inner class AbstractViewHolder(val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(model: MessageModel)
    }

    inner class FirstViewHolder(binding: MessageLayout1Binding) : AbstractViewHolder(binding) {
        override fun bind(model: MessageModel) {
            (binding as MessageLayout1Binding).apply {
                chatMessage.setText(model.message)
                subtitleTextView.setText(model.senderId)
            }
        }

    }

    inner class SecondViewHolder(binding: MessageLayout2Binding) : AbstractViewHolder(binding) {
        override fun bind(model: MessageModel) {
            (binding as MessageLayout2Binding).apply {
                chatMessage.setText(model.message)
                subtitleTextView.setText(model.senderId)
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
        return when (currentItem.senderId == uid) {
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