package com.fourteen06.emseesquare.utils.firebase_url_locator

object FirebaseStorageUrlLocator {
    fun getNoticeUrlPrefix(noticeUID: String) = "notices/${noticeUID}/file"
}
