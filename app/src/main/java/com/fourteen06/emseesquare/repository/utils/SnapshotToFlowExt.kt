package com.fourteen06.emseesquare.repository.utils

import com.google.firebase.firestore.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun Query.asFlow(): Flow<QuerySnapshot> {
    return callbackFlow {
        val callback = addSnapshotListener { querySnapshot, ex ->
            if (ex != null) {
                close(ex)
            } else {
                trySend(querySnapshot!!).isSuccess
            }
        }
        awaitClose {
            callback.remove()
        }
    }
}

fun DocumentReference.asFlow(): Flow<DocumentSnapshot> {
    return callbackFlow {
        val callback = addSnapshotListener(EventListener { snapshot, e ->
            if (e != null) {
                close(e)
            } else {
                trySend(snapshot!!).isSuccess
            }
        })
        awaitClose {
            callback.remove()
        }
    }
}