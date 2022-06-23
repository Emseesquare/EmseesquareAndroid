package com.fourteen06.emseesquare.presentation

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fourteen06.emseesquare.R
import com.fourteen06.emseesquare.databinding.NoticeViewBinding
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.models.NoticeModel
import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.models.UserRole

private val user = User(
    id = "1",
    name = "Shashank",
    subTitle = "Teacher",
    profileImageUrl = "",
    role = UserRole.Student,
    instituteName = "First",
    instituteId = "1"
)

class NoticeAdapter() :
    ListAdapter<NoticeModel, NoticeAdapter.ViewHolder>(CustomDiffUtil()) {
    inner class ViewHolder(val binding: NoticeViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notice: NoticeModel) {
            val spannable =
                SpannableString(user.subTitle + "(Posted on:" + notice.time + ")")
            spannable.setSpan(
                ForegroundColorSpan(itemView.context.getColor(R.color.black35)),
                user.subTitle.length, // start
                spannable.length, // end
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )

            binding.apply {
                userName.text = user.name
                userSubtitle.text = spannable
                noticeDescription.text = notice.content
                when (notice.attachmentType) {
                    is AttachmentType.Image -> {

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
