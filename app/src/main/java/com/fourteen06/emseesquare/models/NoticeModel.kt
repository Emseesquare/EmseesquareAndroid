package com.fourteen06.emseesquare.models

import java.util.*

data class NoticeModel(
    val id: String = "",
    val time: Date? = null,
    val content: String = "",
    val pins: Int = 0,
    val attachmentType: AttachmentType = AttachmentType.None,
    val user: User? = null
)

//We organise what we write into sentences and paragraphs. A paragraph begins on a new line within the text and there is often a blank line between paragraphs. A paragraph usually contains more than one sentence and it is usually about one topic.
//https://firebasestorage.googleapis.com/v0/b/emseesquare-e0913.appspot.com/o/notice_assets%2Fpexels-gije-cho-10836627.jpg?alt=media&token=532e523f-b07b-4f77-8c83-a65b76034c99
