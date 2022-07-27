package com.fourteen06.emseesquare.repository.message

import com.fourteen06.emseesquare.models.MessageModel
import com.fourteen06.emseesquare.models.MessageModel.Factory.toMessage
import com.fourteen06.emseesquare.repository.utils.asFlow
import com.fourteen06.emseesquare.utils.Resource
import com.fourteen06.emseesquare.utils.firebase_routes.FirestoreRoute
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetChatUsecase @Inject constructor(
    private val db: FirebaseFirestore
) {
    operator fun invoke(roomId: String): Flow<Resource<List<MessageModel>>> = flow {
        emit(Resource.Loading())
        try {
            val ref = db.collection(FirestoreRoute.GET_MESSAGES_FROM_ROOM_ID(roomId))
                .orderBy(MessageModel.TIME, Query.Direction.ASCENDING)
                .asFlow().map { snapshots ->
                    snapshots.map {
                        it.toMessage()
                    }
                }
            emitAll(ref.map {
                Resource.Success(it)
            })
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}