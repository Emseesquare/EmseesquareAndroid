package com.fourteen06.emseesquare.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideFirestone() = Firebase.firestore

    @Provides
    fun provideFirebaseStorage() = Firebase.storage

    @CurrentUserUID
    @Provides
    fun provideCurrentUserUid(
        firebaseAuth: FirebaseAuth
    ) = firebaseAuth.currentUser?.uid
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class CurrentUserUID

