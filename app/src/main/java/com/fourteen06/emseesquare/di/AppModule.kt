package com.fourteen06.emseesquare.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AppModule {

    fun provideFirebaseAuth() = Firebase.auth

}