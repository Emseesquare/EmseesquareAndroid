package com.fourteen06.emseesquare.repository.community

import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.models.CommunityModel
import com.fourteen06.emseesquare.models.CommunityPostModel
import com.fourteen06.emseesquare.models.Reaction
import com.fourteen06.emseesquare.repository.utils.AppSharedPreference
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddCommunityPostUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sharedPreference: AppSharedPreference
) {
    suspend operator fun invoke(
        community: CommunityModel,
        message: String,
        attachmentType: AttachmentType
    ): Resource<CommunityPostModel> {
        val post = CommunityPostModel(
            communityPostId = UUID.randomUUID().toString(),
            time = Date(System.currentTimeMillis()),
            content = message,
            attachmentType = attachmentType,
            user = sharedPreference.getUser(),
            reactions = listOf(
                Reaction(
                    symbol = "😀",
                    count = 1
                ),
                Reaction(
                    symbol = "\uD83D\uDE21",
                    count = 1
                ),
            )

        )
        return try {
            val ref =
                firestore.collection(FirestoreRoute.GET_POSTS_FROM_COMMUNITY_ID(community.communityId))
                    .document(post.communityPostId)
            ref.set(post.toHashMap())
            Resource.Success(post)
        } catch (e: FirebaseFirestoreException) {
            Resource.Error(message = e.message.toString())
        }
    }
}