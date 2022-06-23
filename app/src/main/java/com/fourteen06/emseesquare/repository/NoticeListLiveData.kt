package com.fourteen06.emseesquare.repository

import android.util.Log
import com.fourteen06.emseesquare.models.AttachmentType
import com.fourteen06.emseesquare.models.NoticeModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map

val TAG = "NoticeListLiveData"
fun getPagedNotices(): Flow<List<NoticeModel>> {
    return Firebase.firestore.collection("notice")
        .orderBy("time", Query.Direction.DESCENDING).limit(50).asFlow().map {
            it.map {
                it.toNotice()
            }
        }
}

private fun QueryDocumentSnapshot.toNotice(): NoticeModel {
    val dataMap = this.data
    Log.d(TAG, "toNotice: $dataMap")
    return NoticeModel(
        id = dataMap["id"] as String,
        content = dataMap["content"].toString(),
        time = (dataMap["time"] as Timestamp).toDate(),
        attachmentType = when (dataMap["attachmentType"]) {
            1L -> {
                AttachmentType.Image(imageUrl = dataMap["imageUrl"] as String)
            }
            2L -> {
                AttachmentType.File(fileUrl = dataMap["fileUrl"] as String)
            }
            else -> {
                AttachmentType.None
            }
        }
    )
}

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