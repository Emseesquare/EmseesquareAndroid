package com.fourteen06.emseesquare.repository.community

import com.fourteen06.emseesquare.models.CommunityModel
import com.fourteen06.emseesquare.models.User
import com.fourteen06.emseesquare.models.User.Factory.toUser
import com.fourteen06.emseesquare.models.User.Factory.toUserCommunities
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute.COMMUNITY_COLLECTION
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute.PROFILE_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddNewCommunityRoomUseCase @Inject constructor(
    val firestore: FirebaseFirestore,
) {
    suspend operator fun invoke(
        communityId: String,
        community: CommunityModel
    ): Flow<Resource<CommunityModel>> = flow {
        emit(Resource.Loading())
        try {
            val communityRef = firestore.collection(COMMUNITY_COLLECTION).document(communityId)
            communityRef.set(community.toHashMap()).await()
            val creatorRef = firestore.collection(PROFILE_COLLECTION).document(community.admin[0])
            val profile = creatorRef.get().await()
            creatorRef.set(
                profile.toUser().toHashMap().also {
                    it[User.COMMUNITY_REFS] =
                        profile.toUserCommunities().toMutableList().also { list ->
                            list.add(communityId)
                        }
                    emit(Resource.Success(community))
                })
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}