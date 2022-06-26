package com.fourteen06.emseesquare.repository

import com.fourteen06.emseesquare.models.NoticeModel
import com.fourteen06.emseesquare.models.NoticeModel.Factory.toNotice
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

val TAG = "NoticeListLiveData"
fun getPagedNotices(lastVisibleItem: StateFlow<Int>): Flow<List<NoticeModel>> {
    val firestoreRoot = Firebase.firestore
    return flow {
        val notices = mutableListOf<DocumentSnapshot>()
        notices.addAll(
            suspendCoroutine<List<DocumentSnapshot>> { c ->
                firestoreRoot.collection("notice")
                    .orderBy("time", Query.Direction.DESCENDING)
                    .limit(50)
                    .get().addOnSuccessListener {
                        c.resume(it.documents)
                    }
            }
        )
        emit(notices.map {
            (it as QueryDocumentSnapshot).toNotice()
        })
        lastVisibleItem.transform { lastVisibleItem ->
            if (lastVisibleItem == notices.size) {
                notices.addAll(
                    suspendCoroutine<List<DocumentSnapshot>> { c ->
                        firestoreRoot.collection("notice")
                            .orderBy("time", Query.Direction.DESCENDING).startAfter(notices.last())
                            .limit(25).get().addOnSuccessListener {
                                c.resume(it.documents)
                            }
                    }
                )
                emit(notices.map { (it as QueryDocumentSnapshot).toNotice() })
            }

        }.collect {
            emit(it)
        }
    }
}



