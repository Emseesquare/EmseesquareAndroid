package com.fourteen06.emseesquare.repository.notice

import com.fourteen06.emseesquare.models.NoticeModel
import com.fourteen06.emseesquare.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNoticeUseCase @Inject constructor(
    private val firestoreRef: FirebaseFirestore
) {
    operator fun invoke(): Flow<Resource<NoticeModel>> = flow {

    }
}


