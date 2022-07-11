package com.fourteen06.emseesquare.repository.message

import com.fourteen06.emseesquare.models.MessageRoom
import com.fourteen06.emseesquare.models.MessageRoom.Factory.toMessageRoom
import com.fourteen06.emseesquare.repository.user_setup.UserInfoSetupUsercase
import com.fourteen06.emseesquare.repository.utils.asFlow
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAllCurrentMessageRoomUseCase @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val getUserUseCase: UserInfoSetupUsercase
) {

    operator fun invoke(uid: String) =
        firestore.collection(FirestoreRoute.MESSAGE_COLLECTION)
            .whereArrayContains(MessageRoom.PARTICIPANTS, uid)
            .asFlow().map { snapshots ->
                snapshots.map {
                    it.toMessageRoom {
                        when (val user = getUserUseCase.getUserById(it)) {
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
                }.sortedByDescending {
                    it.lastMessageTimestamp.time
                }
            }

}