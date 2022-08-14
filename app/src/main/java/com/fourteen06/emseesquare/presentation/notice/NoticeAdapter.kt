package com.fourteen06.emseesquare.presentation.notice

import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fourteen06.emseesquare.databinding.NoticeViewBinding
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.models.NoticeModel
import com.fourteen06.emseesquare.utils.UnixToHuman.getTimeAgo
import com.fourteen06.emseesquare.utils.load
import com.fourteen06.emseesquare.utils.loadProfileImage
import com.stfalcon.imageviewer.StfalconImageViewer


class NoticeAdapter(private val onPinNotice: (noticeId: String) -> Unit) :
    ListAdapter<NoticeModel, NoticeAdapter.ViewHolder>(CustomDiffUtil()) {
    inner class ViewHolder(val binding: NoticeViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notice: NoticeModel) {
            val spannable =
                SpannableString(
                    notice.user?.subTitle.toString() + "(" + getTimeAgo(
                        notice.time.time
                    ) + ")"
                )
            binding.apply {
                userName.text = notice.user?.name.toString()
                userSubtitle.text = spannable
                noticeDescription.text = notice.content
                userAvatarImageView.loadProfileImage(notice.user?.profileImageUrl)
                pinsTextView.text = notice.pins.toString()
                pinImageView.setOnClickListener {
                    onPinNotice(notice.id)
                }
                when (notice.attachmentType) {
                    is AttachmentType.Image -> {
                        contentImage.load(notice.attachmentType.imageUrl)
                        imageHolderCardView.setOnClickListener {
                            StfalconImageViewer.Builder<String>(
                                itemView.context,
                                arrayOf(notice.attachmentType.imageUrl)
                            ) { imageView, url ->
                                imageView.load(url, false)
                            }.withTransitionFrom(binding.contentImage).withHiddenStatusBar(false)
                                .show()
                        }
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


    class CustomDiffUtil : DiffUtil.ItemCallback<NoticeModel>() {
        override fun areItemsTheSame(
            oldItem: NoticeModel,
            newItem: NoticeModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NoticeModel,
            newItem: NoticeModel
        ): Boolean {
            return oldItem == newItem

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            NoticeViewBinding.inflate(
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
