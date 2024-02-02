package com.kien.petclub.di

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.kien.petclub.utils.AnimationLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideContext(application: Application): Context = application.applicationContext



}