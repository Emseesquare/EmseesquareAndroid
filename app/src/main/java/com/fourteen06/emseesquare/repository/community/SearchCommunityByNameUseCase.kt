package com.fourteen06.emseesquare.repository.community

import com.fourteen06.emseesquare.models.CommunityModel
import com.fourteen06.emseesquare.models.CommunityModel.Companion.toCommunityModel
import com.fourteen06.emseesquare.repository.utils.asFlow
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchCommunityByNameUseCase @Inject constructor(
    private val firestoreRef: FirebaseFirestore,
) {
    operator fun invoke(query: String): Flow<List<CommunityModel>> {
        val communityRef = firestoreRef.collection(FirestoreRoute.COMMUNITY_COLLECTION)
            .whereGreaterThanOrEqualTo(CommunityModel.COMMUNITY_NAME, query)
            .whereLessThanOrEqualTo(CommunityModel.COMMUNITY_NAME, query + "\uf8ff")
        return communityRef.asFlow().map {
            it.map {
                it.toCommunityModel()
            }
        }
    }
}