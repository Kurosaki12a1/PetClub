package com.kien.main.di

import android.content.Context
import com.kien.petclub.data.data_source.local.preferences.AppPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppPreferencesModule {
    @Provides
    fun providePreferences(@ApplicationContext context: Context): AppPreferences =
        AppPreferences(context)

}