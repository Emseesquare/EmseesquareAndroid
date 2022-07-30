package com.fourteen06.emseesquare.repository.community

import com.fourteen06.emseesquare.models.CommunityModel
import com.fourteen06.emseesquare.models.CommunityPostModel.Factory.toCommunityPostModel
import com.fourteen06.emseesquare.models.MessageModel
import com.fourteen06.emseesquare.repository.user_setup.UserInfoSetupUsercase
import com.fourteen06.emseesquare.repository.utils.asFlow
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCommunityPostUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val userUserCase: UserInfoSetupUsercase

) {
    operator fun invoke(communityModel: CommunityModel) =
        firestore.collection(FirestoreRoute.GET_POSTS_FROM_COMMUNITY_ID(communityModel.communityId))
            .orderBy(MessageModel.TIME, Query.Direction.ASCENDING)
            .asFlow().map { snapshots ->
                snapshots.map {
                    it.toCommunityPostModel { uid ->
                        when (val user = userUserCase.getUserById(uid)) {
                            is Resource.Error -> {
                                throw IllegalStateException("GetNoticeUseCase:User doesn't exists")
                            }
                            is Resource.Loading -> {
                                throw IllegalStateException("GetNoticeUseCase:This should never be called.")

                            }
                            is Resource.Success -> {
                                user.data
                            }
                        }
                    }
                }
            }


}
