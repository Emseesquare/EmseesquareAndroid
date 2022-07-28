package com.fourteen06.emseesquare.repository.community

import com.fourteen06.emseesquare.models.CommunityModel
import com.fourteen06.emseesquare.models.CommunityModel.Companion.toCommunityModel
import com.fourteen06.emseesquare.models.User.Factory.toUserCommunities
import com.fourteen06.emseesquare.repository.utils.asFlow
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetUserCommunitiesUseCase @Inject constructor(
    private val firestoreRef: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    operator fun invoke(): Flow<List<CommunityModel>> {
        val currentUserRef = firestoreRef.collection(FirestoreRoute.PROFILE_COLLECTION).document(
            firebaseAuth.currentUser?.uid.toString()
        )
        val communityRef = firestoreRef.collection(FirestoreRoute.COMMUNITY_COLLECTION)
        return currentUserRef.asFlow().map { userDocumentSnapshot ->
            userDocumentSnapshot.toUserCommunities()
        }.map {
            if (it.isNotEmpty()) {
                val communityDocuments = communityRef.whereIn(
                    FieldPath.documentId(), it
                ).get().await()
                communityDocuments.map {
                    it.toCommunityModel()
                }.sortedBy {
                    it.communityName
                }
            } else {
                emptyList()
            }
        }

    }
}