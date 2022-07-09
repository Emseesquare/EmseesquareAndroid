package com.fourteen06.emseesquare.utils.firebase_url_locator

object FirebaseStorageUrlLocator {
    fun getNoticeUrlPrefix(noticeUID: String) = "${NOTICE_COLLECTION_NAME}/${noticeUID}/file"
}
