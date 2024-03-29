package com.fourteen06.emseesquare.models

sealed class AttachmentType {

    data class Image(val imageUrl: String) : AttachmentType()
    data class File(val fileUrl: String, val thumbnail: String) : AttachmentType()
    object None : AttachmentType();

    fun toURL(): String {
        return when (this) {
            is File -> fileUrl
            is Image -> imageUrl
            None -> ""
        }
    }

    fun toThumbnailURL(): String {
        return when (this) {
            is File -> thumbnail
            else -> ""
        }
    }

    fun toType(): Int {
        return when (this) {
            is File -> 2
            is Image -> 1
            None -> 0
        }
    }
}

