package com.fourteen06.emseesquare.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    fun provideFirebaseAuth() = Firebase.auth
    fun provideFirestone() = Firebase.firestore
}