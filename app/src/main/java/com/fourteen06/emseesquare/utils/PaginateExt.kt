package com.fourteen06.emseesquare.utils

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private fun Query.paginate(lastVisibleItem: Flow<Int>): Flow<List<DocumentSnapshot>> = flow {
    val documents = mutableListOf<DocumentSnapshot>()
    documents.addAll(
        suspendCoroutine { c ->
            this@paginate.limit(25).get().addOnSuccessListener { c.resume(it.documents) }
        }
    )
    emit(documents)
    lastVisibleItem.transform { lastVisible ->
        if (lastVisible == documents.size && documents.size > 0) {
            documents.addAll(
                suspendCoroutine { c ->
                    this@paginate.startAfter(documents.last())
                        .limit(25)
                        .get()
                        .addOnSuccessListener {
                            c.resume(it.documents)
                        }
                }
            )
            emit(documents)
        }
    }.collect { docs ->
        emit(docs)
    }
}