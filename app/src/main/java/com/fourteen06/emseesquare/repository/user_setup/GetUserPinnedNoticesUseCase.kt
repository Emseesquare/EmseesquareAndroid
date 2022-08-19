package com.fourteen06.emseesquare.repository.user_setup

import com.fourteen06.emseesquare.repository.utils.asFlow
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetUserPinnedNoticesUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    operator fun invoke(): Flow<List<String>> {
        val pathToNoticesPinned =
            FirestoreRoute.GET_NOTICES_PINNED_FROM_UID(auth.currentUser?.uid.toString())
        return firestore.collection(pathToNoticesPinned)
            .asFlow().map { snapshots ->
                snapshots.map {
                    it.id
                }

            }
    }
}