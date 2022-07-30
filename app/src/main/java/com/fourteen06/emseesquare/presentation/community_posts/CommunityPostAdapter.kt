package com.fourteen06.emseesquare.presentation.community_posts

import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fourteen06.emseesquare.databinding.CommunityPostViewBinding
import com.fourteen06.emseesquare.databinding.CommunityReactionLayoutBinding
import com.fourteen06.emseesquare.databinding.NoticeViewBinding
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.models.CommunityPostModel
import com.fourteen06.emseesquare.utils.UnixToHuman.getTimeAgo
import com.fourteen06.emseesquare.utils.load
import com.fourteen06.emseesquare.utils.loadProfileImage


class CommunityPostAdapter :
    ListAdapter<CommunityPostModel, CommunityPostAdapter.ViewHolder>(CustomDiffUtil()) {
    inner class ViewHolder(val binding: CommunityPostViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: CommunityPostModel) {
            val spannable =
                SpannableString(
                    post.user?.subTitle.toString() + "(" + getTimeAgo(
                        post.time.time
                    ) + ")"
                )
            binding.apply {
                userName.text = post.user?.name.toString()
                userSubtitle.text = spannable
                noticeDescription.text = post.content
                userAvatarImageView.loadProfileImage(post.user?.profileImageUrl)
                when (post.attachmentType) {
                    is AttachmentType.Image -> {
                        contentImage.load(post.attachmentType.imageUrl)
                    }
                    AttachmentType.None -> {
                        imageHolderCardView.visibility = View.GONE
                    }
                    is AttachmentType.File -> {

                    }
                }
            }
        }
    }


    class CustomDiffUtil : DiffUtil.ItemCallback<CommunityPostModel>() {
        override fun areItemsTheSame(
            oldItem: CommunityPostModel,
            newItem: CommunityPostModel
        ): Boolean {
            return oldItem.communityPostId == newItem.communityPostId
        }

        override fun areContentsTheSame(
            oldItem: CommunityPostModel,
            newItem: CommunityPostModel
        ): Boolean {
            return oldItem == newItem

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CommunityPostViewBinding.inflate(
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
