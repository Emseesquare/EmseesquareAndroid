package com.fourteen06.emseesquare.repository.notice

import com.fourteen06.emseesquare.models.NoticeModel
import com.fourteen06.emseesquare.models.NoticeModel.Factory.toNotice
import com.fourteen06.emseesquare.repository.user_setup.UserInfoSetupUsercase
import com.fourteen06.emseesquare.repository.utils.asFlow
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute.NOTICE_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNoticeUseCase @Inject constructor(
    private val firestoreRef: FirebaseFirestore,
    private val userUserCase: UserInfoSetupUsercase
) {
    operator fun invoke(): Flow<List<NoticeModel>> {
        return firestoreRef.collection(NOTICE_COLLECTION)
            .orderBy(NoticeModel.TIME, Query.Direction.DESCENDING)
            .asFlow().map { snapshots ->
                snapshots.map {
                    it.toNotice { uid ->
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
}


