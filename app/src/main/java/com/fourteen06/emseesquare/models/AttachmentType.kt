package com.fourteen06.emseesquare.models

sealed class AttachmentType {
    data class Image(val imageUrl: String) : AttachmentType()
    data class File(val fileUrl: String) : AttachmentType()
    object None : AttachmentType()

}