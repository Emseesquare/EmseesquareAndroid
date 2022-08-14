package com.fourteen06.emseesquare.repository.notice

import com.fourteen06.emseesquare.models.NoticeModel
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddNoticeUseCase @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) {
    private val db = firebaseFirestore
    operator fun invoke(notice: NoticeModel): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        try {
            val ref = db.document(FirestoreRoute.GET_NOTICE_FROM_NOTICE_ID(notice.id))

            ref.set(notice.toHashMap())
                .await()
            emit(Resource.Success(data = Unit))
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}
