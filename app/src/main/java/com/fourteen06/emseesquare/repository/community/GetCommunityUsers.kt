package com.fourteen06.emseesquare.repository.community

import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.models.User.Factory.toUser
import com.fourteen06.emseesquare.repository.utils.asFlow
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCommunityUsers @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    operator fun invoke(
        communityId: String,
        searchQuery: String? = null
    ) = if (searchQuery != null) {
        firestore.collection(FirestoreRoute.PROFILE_COLLECTION)
            .whereArrayContains(User.COMMUNITY_REFS, communityId)
            .whereGreaterThanOrEqualTo(User.NAME, searchQuery)
            .whereLessThanOrEqualTo(User.NAME, searchQuery + "\uf8ff")
            .asFlow().map { snapshots ->
                snapshots.map {
                    it.toUser()
                }.sortedByDescending {
                    it.name
                }
            }
    } else {
        firestore.collection(FirestoreRoute.PROFILE_COLLECTION)
            .whereArrayContains(User.COMMUNITY_REFS, communityId)
            .asFlow().map { snapshots ->
                snapshots.map {
                    it.toUser()
                }.sortedByDescending {
                    it.name
                }
            }
    }

}