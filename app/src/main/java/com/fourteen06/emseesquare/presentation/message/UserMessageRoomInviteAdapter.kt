package com.fourteen06.emseesquare.presentation.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fourteen06.emseesquare.databinding.MessageRecyclerViewLayoutBinding
import com.fourteen06.emseesquare.models.User

class UserMessageRoomInviteAdapter(
    private val onClick: (User) -> Unit
) :
    ListAdapter<User, UserMessageRoomInviteAdapter.ViewHolder>(CustomDiffUtil()) {
    inner class ViewHolder(val binding: MessageRecyclerViewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                userNameTextView.text = user.name
                root.setOnClickListener {
                    onClick(user)
                }
                timeTextView.visibility = View.GONE
                subtitleTextView.visibility = View.GONE
                profileImageView.load(user.profileImageUrl)
            }
        }
    }


    class CustomDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(
            oldItem: User,
            newItem: User
        ): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(
            oldItem: User,
            newItem: User
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
