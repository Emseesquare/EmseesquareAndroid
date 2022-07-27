package com.fourteen06.emseesquare.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fourteen06.emseesquare.R

fun ImageView.load(uri: String) {

    this.post {

        val myOptions = RequestOptions()
            .override(this.width, this.height)
            .centerCrop()

        Glide
            .with(this.context)
            .load(uri)
            .apply(myOptions)
            .into(this)

    }

}

fun ImageView.loadProfileImage(uri: String?) {

    this.post {

        val myOptions = RequestOptions()
            .override(this.width, this.height)
            .centerCrop()

        Glide
            .with(this.context)
            .load(uri)
            .placeholder(R.drawable.ic_profile_placeholder)
            .apply(myOptions)
            .into(this)

    }

}