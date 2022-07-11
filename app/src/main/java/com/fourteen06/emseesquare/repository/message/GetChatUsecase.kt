package com.fourteen06.emseesquare.repository.message

import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetChatUsecase @Inject constructor(
    private val firestore: FirebaseFirestore
) {
}