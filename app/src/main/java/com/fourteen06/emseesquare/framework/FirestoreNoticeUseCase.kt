package com.fourteen06.emseesquare.framework

import com.fourteen06.emseesquare.di.AppModule
import com.fourteen06.emseesquare.models.NoticeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirestoreNoticeUseCase {
    val firestore = AppModule.provideFirestone()
//    suspend operator fun invoke(): Flow<List<NoticeModel>> = flow {
//        val listOfNotice =
//            firestore.collection(NOTICE_COLLECTION_NAME).orderBy(NOTICE_ORDERBY_TIME).limit(
//                NOTICE_LIMIT
//            ).get().await()
//
//
//    }

    companion object {
        const val NOTICE_COLLECTION_NAME = "notice"
        const val NOTICE_ORDERBY_TIME = "time"
        const val NOTICE_LIMIT = 25L
    }
}