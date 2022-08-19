package com.fourteen06.emseesquare.repository.notice

import com.fourteen06.emseesquare.models.NoticeModel
import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PinNoticeUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend operator fun invoke(noticeId: String, noticeTime: Long): Resource<Unit> {
        return try {
            val noticeRef = FirestoreRoute.GET_NOTICE_FROM_NOTICE_ID(noticeId)
            firestore.document(noticeRef)
                .update(NoticeModel.PINS, FieldValue.increment(1)).await()
            val userRef =
                FirestoreRoute.GET_NOTICES_PINNED_FROM_UID(auth.currentUser?.uid.toString())
            firestore.collection(userRef).document(noticeId).set(
                mutableMapOf<String, Any>(
                    User.PINNED_NOTICE_TIMESTAMP to noticeTime
                )
            ).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}