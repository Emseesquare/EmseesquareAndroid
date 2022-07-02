package com.fourteen06.emseesquare.models

sealed class AttachmentType {

    data class Image(val imageUrl: String) : AttachmentType()
    data class File(val fileUrl: String) : AttachmentType()
    object None : AttachmentType()
}

const val IMAGE_URL = "imageUrl"
const val FILE_URL = "fileUrl"