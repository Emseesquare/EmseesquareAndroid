package com.fourteen06.emseesquare.presentation.community

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.fourteen06.emseesquare.databinding.CommunityOverviewCardLayoutBinding
import com.fourteen06.emseesquare.models.CommunityModel

class CommunityAdapter(private val onClick: (CommunityModel) -> Unit) :
    ListAdapter<CommunityModel, CommunityAdapter.ViewHolder>(CustomDiffUtil()) {
    inner class ViewHolder(val binding: CommunityOverviewCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(messageRoom: CommunityModel) {
            binding.apply {
                communityImageView.load(messageRoom.communityImage)
                communityTextView.text = messageRoom.communityName
            }
        }
    }


    class CustomDiffUtil : DiffUtil.ItemCallback<CommunityModel>() {
        override fun areItemsTheSame(
            oldItem: CommunityModel,
            newItem: CommunityModel
        ): Boolean {
            return oldItem.communityId == newItem.communityId
        }

        override fun areContentsTheSame(
            oldItem: CommunityModel,
            newItem: CommunityModel
        ): Boolean {
            return oldItem == newItem

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CommunityOverviewCardLayoutBinding.inflate(
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
